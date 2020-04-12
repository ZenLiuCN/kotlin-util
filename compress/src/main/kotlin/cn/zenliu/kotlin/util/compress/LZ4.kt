/*
 *  Copyright (c) 2020.  Zen.Liu .
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 *
 *  http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 *
 *   @Project: kotlin-utils
 *   @Module: compress
 *   @File: LZ4.kt
 *   @Author:  lcz20@163.com
 *   @LastModified:  2020-04-11 20:43:03
 */

package cn.zenliu.kotlin.util.compress

import java.io.*
import java.util.*


object LZ4 {
	const val MEMORY_USAGE = 14
	const val MIN_MATCH = 4 // minimum length of a match
	const val MAX_DISTANCE = 1 shl 16 // maximum distance of a reference
	const val LAST_LITERALS = 5 // the last 5 bytes must be encoded as literals
	const val HASH_LOG_HC = 15 // log size of the dictionary for compressHC
	const val HASH_TABLE_SIZE_HC = 1 shl HASH_LOG_HC
	const val OPTIMAL_ML = 0x0F + 4 - 1 // match length that doesn't require an additional byte

	private fun checkFromToIndex(fromIndex: Int, toIndex: Int, length: Int): Int {
		if (fromIndex < 0 || fromIndex > toIndex || toIndex > length) {
			throw IndexOutOfBoundsException("Range [$fromIndex, $toIndex) out-of-bounds for length $length")
		}
		return fromIndex
	}

	private fun checkFromIndexSize(fromIndex: Int, size: Int, length: Int): Int {
		val end = fromIndex + size
		if (fromIndex < 0 || fromIndex > end || end > length) {
			throw IndexOutOfBoundsException("Range [$fromIndex, $fromIndex + $size) out-of-bounds for length $length")
		}
		return fromIndex
	}

	private fun mismatch(a: ByteArray, aFromIndex: Int, aToIndex: Int, b: ByteArray, bFromIndex: Int, bToIndex: Int): Int {
		checkFromToIndex(aFromIndex, aToIndex, a.size)
		checkFromToIndex(bFromIndex, bToIndex, b.size)
		val aLen = aToIndex - aFromIndex
		val bLen = bToIndex - bFromIndex
		val len = aLen.coerceAtMost(bLen)
		return (0 until len).firstOrNull { a[it + aFromIndex] != b[it + bFromIndex] }
			?: if (aLen == bLen) -1 else len
	}

	private fun hash(i: Int, hashBits: Int): Int {
		return i * -1640531535 ushr 32 - hashBits
	}

	private fun hashHC(i: Int): Int {
		return hash(i, HASH_LOG_HC)
	}

	private fun readInt(buf: ByteArray, i: Int): Int {
		return buf[i].toInt() and 0xFF shl 24 or (buf[i + 1].toInt() and 0xFF shl 16) or (buf[i + 2].toInt() and 0xFF shl 8) or (buf[i + 3].toInt() and 0xFF)
	}

	private fun commonBytes(b: ByteArray, o1: Int, o2: Int, limit: Int): Int {
		assert(o1 < o2)
		// never -1 because lengths always differ
		return mismatch(b, o1, limit, b, o2, limit)
	}

	fun decompress(compressed: DataInput, decompressedLen: Int, dest: ByteArray): Int {
		var dOff = 0
		val destEnd = dest.size
		do {
			// literals
			val token: Int = compressed.readByte().toInt() and 0xFF
			var literalLen = token ushr 4
			if (literalLen != 0) {
				if (literalLen == 0x0F) {
					var len: Byte
					while (compressed.readByte().also { len = it } == 0xFF.toByte()) {
						literalLen += 0xFF
					}
					literalLen += (len.toInt() and 0xFF)
				}
				compressed.readFully(dest, dOff, literalLen)
				dOff += literalLen
			}
			if (dOff >= decompressedLen) {
				break
			}

			// matchs
			val matchDec: Int = compressed.readByte().toInt() and 0xFF or (compressed.readByte().toInt() and 0xFF shl 8)
			assert(matchDec > 0)
			var matchLen = token and 0x0F
			if (matchLen == 0x0F) {
				var len: Int
				while (compressed.readByte().also { len = it.toInt() } == 0xFF.toByte()) {
					matchLen += 0xFF
				}
				matchLen += len and 0xFF
			}
			matchLen += MIN_MATCH

			// copying a multiple of 8 bytes can make decompression from 5% to 10% faster
			val fastLen = matchLen + 7 and -0x8
			if (matchDec < matchLen || dOff + fastLen > destEnd) {
				// overlap -> naive incremental copy
				var ref = dOff - matchDec
				val end = dOff + matchLen
				while (dOff < end) {
					dest[dOff] = dest[ref]
					++ref
					++dOff
				}
			} else {
				// no overlap -> arraycopy
				System.arraycopy(dest, dOff - matchDec, dest, dOff, fastLen)
				dOff += matchLen
			}
		} while (dOff < decompressedLen)
		return dOff
	}

	@Throws(IOException::class)
	private fun encodeLen(len: Int, out: DataOutput) {
		var l = len
		while (l >= 0xFF) {
			out.writeByte(0xFF)
			l -= 0xFF
		}
		out.writeByte(l)
	}

	@Throws(IOException::class)
	private fun encodeLiterals(bytes: ByteArray, token: Int, anchor: Int, literalLen: Int, out: DataOutput) {
		out.writeByte(token)

		// encode literal length
		if (literalLen >= 0x0F) {
			encodeLen(literalLen - 0x0F, out)
		}

		// encode literals
		out.write(bytes, anchor, literalLen)
	}

	@Throws(IOException::class)
	private fun encodeLastLiterals(bytes: ByteArray, anchor: Int, literalLen: Int, out: DataOutput) {
		val token = Math.min(literalLen, 0x0F) shl 4
		encodeLiterals(bytes, token, anchor, literalLen, out)
	}

	@Throws(IOException::class)
	private fun encodeSequence(bytes: ByteArray, anchor: Int, matchRef: Int, matchOff: Int, matchLen: Int, out: DataOutput) {
		val literalLen = matchOff - anchor
		assert(matchLen >= 4)
		// encode token
		val token = Math.min(literalLen, 0x0F) shl 4 or Math.min(matchLen - 4, 0x0F)
		encodeLiterals(bytes, token, anchor, literalLen, out)

		// encode match dec
		val matchDec = matchOff - matchRef
		assert(matchDec > 0 && matchDec < 1 shl 16)
		out.writeByte(matchDec)
		out.writeByte((matchDec ushr 8))

		// encode match len
		if (matchLen >= MIN_MATCH + 0x0F) {
			encodeLen(matchLen - 0x0F - MIN_MATCH, out)
		}
	}

	/**
	 * Compress `bytes[off:off+len]` into `out` using
	 * at most 16KB of memory. `ht` shouldn't be shared across threads
	 * but can safely be reused.
	 */
	@Throws(IOException::class)
	fun compress(bytes: ByteArray, offset: Int, len: Int, out: DataOutput, hc: Boolean) {
		val ht: HashTable = if (hc) HighCompressionHashTable() else FastCompressionHashTable()
		var off = offset
		checkFromIndexSize(off, len, bytes.size)
		val base = off
		val end = off + len
		var anchor = off++
		if (len > LAST_LITERALS + MIN_MATCH) {
			val limit = end - LAST_LITERALS
			val matchLimit = limit - MIN_MATCH
			ht.reset(bytes, base, len)
			main@ while (off <= limit) {
				// find a match
				var ref: Int
				while (true) {
					if (off >= matchLimit) {
						break@main
					}
					ref = ht[off]
					if (ref != -1) {
						assert(ref in base until off)
						assert(readInt(bytes, ref) == readInt(bytes, off))
						break
					}
					++off
				}

				// compute match length
				var matchLen = MIN_MATCH + commonBytes(bytes, ref + MIN_MATCH, off + MIN_MATCH, limit)

				// try to find a better match
				var r = ht.previous(ref)
				val min = (off - MAX_DISTANCE + 1).coerceAtLeast(base)
				while (r >= min) {
					assert(readInt(bytes, r) == readInt(bytes, off))
					val rMatchLen = MIN_MATCH + commonBytes(bytes, r + MIN_MATCH, off + MIN_MATCH, limit)
					if (rMatchLen > matchLen) {
						ref = r
						matchLen = rMatchLen
					}
					r = ht.previous(r)
				}
				encodeSequence(bytes, anchor, ref, off, matchLen, out)
				off += matchLen
				anchor = off
			}
		}

		// last literals
		val literalLen = end - anchor
		assert(literalLen >= LAST_LITERALS || literalLen == len)
		encodeLastLiterals(bytes, anchor, end - anchor, out)
	}

	/**
	 * A record of previous occurrences of sequences of 4 bytes.
	 */
	internal abstract class HashTable {
		/** Reset this hash table in order to compress the given content.  */
		abstract fun reset(b: ByteArray, off: Int, len: Int)

		/**
		 * Advance the cursor to {@off} and return an index that stored the same
		 * 4 bytes as `b[o:o+4)`. This may only be called on strictly
		 * increasing sequences of offsets. A return value of `-1` indicates
		 * that no other index could be found.  */
		abstract operator fun get(off: Int): Int

		/**
		 * Return an index that less than `off` and stores the same 4
		 * bytes. Unlike [.get], it doesn't need to be called on increasing
		 * offsets. A return value of `-1` indicates that no other index could
		 * be found.  */
		abstract fun previous(off: Int): Int

		// For testing
		abstract fun assertReset(): Boolean
	}

	/**
	 * Simple lossy [HashTable] that only stores the last ocurrence for
	 * each hash on `2^14` bytes of memory.
	 */
	internal class FastCompressionHashTable : HashTable() {
		private lateinit var bytes: ByteArray
		private var base = 0
		private var lastOff = 0
		private var end = 0
		private var hashLog = 0
		private var hashTable: MutableList<Int> = mutableListOf()
		override fun reset(b: ByteArray, off: Int, len: Int) {
			checkFromIndexSize(off, len, b.size)
			this.bytes = b
			base = off
			lastOff = off - 1
			end = off + len
			val bitsPerOffsetLog = 32 - Integer.numberOfLeadingZeros(len - LAST_LITERALS - 1)
			hashLog = MEMORY_USAGE + 3 - bitsPerOffsetLog
			get(off)
		}

		override fun get(off: Int): Int {
			assert(off > lastOff)
			assert(off < end)
			val v = readInt(bytes, off)
			val h = hash(v, hashLog)
			val ref = base + hashTable[h]
			hashTable[h] = off - base
			lastOff = off
			return if (ref < off && off - ref < MAX_DISTANCE && readInt(bytes, ref) == v) {
				ref
			} else {
				-1
			}
		}

		override fun previous(off: Int): Int {
			return -1
		}

		override fun assertReset(): Boolean {
			return true
		}
	}

	/**
	 * A higher-precision [HashTable]. It stores up to 256 occurrences of
	 * 4-bytes sequences in the last `2^16` bytes, which makes it much more
	 * likely to find matches than [FastCompressionHashTable].
	 */
	internal class HighCompressionHashTable : HashTable() {
		private lateinit var bytes: ByteArray
		private var base = 0
		private var next = 0
		private var end = 0
		private val hashTable: IntArray = IntArray(HASH_TABLE_SIZE_HC)
		private val chainTable: ShortArray = ShortArray(MAX_DISTANCE)
		private var attempts = 0

		init {
			Arrays.fill(hashTable, -1)
			Arrays.fill(chainTable, 0xFFFF.toShort())
		}

		override fun reset(b: ByteArray, off: Int, len: Int) {
			checkFromIndexSize(off, len, b.size)
			if (end - base < chainTable.size) {
				// The last call to compress was done on less than 64kB, let's not reset
				// the hashTable and only reset the relevant parts of the chainTable.
				// This helps avoid slowing down calling compress() many times on short
				// inputs.
				val startOffset = base and MASK
				val endOffset = if (end == 0) 0 else (end - 1 and MASK) + 1
				if (startOffset < endOffset) {
					Arrays.fill(chainTable, startOffset, endOffset, 0xFFFF.toShort())
				} else {
					Arrays.fill(chainTable, 0, endOffset, 0xFFFF.toShort())
					Arrays.fill(chainTable, startOffset, chainTable.size, 0xFFFF.toShort())
				}
			} else {
				// The last call to compress was done on a large enough amount of data
				// that it's fine to reset both tables
				Arrays.fill(hashTable, -1)
				Arrays.fill(chainTable, 0xFFFF.toShort())
			}
			this.bytes = b
			base = off
			next = off
			end = off + len
		}

		override fun get(off: Int): Int {
			assert(off > next)
			assert(off < end)
			while (next < off) {
				addHash(next)
				next++
			}
			val v = readInt(bytes, off)
			val h = hashHC(v)
			attempts = 0
			var ref = hashTable[h]
			if (ref >= off) {
				// remainder from a previous call to compress()
				return -1
			}
			val min = Math.max(base, off - MAX_DISTANCE + 1)
			while (ref >= min && attempts < MAX_ATTEMPTS) {
				if (readInt(bytes, ref) == v) {
					return ref
				}
				ref -= chainTable[ref and MASK].toInt() and 0xFFFF
				attempts++
			}
			return -1
		}

		private fun addHash(off: Int) {
			val v = readInt(bytes, off)
			val h = hashHC(v)
			var delta = off - hashTable[h]
			if (delta <= 0 || delta >= MAX_DISTANCE) {
				delta = MAX_DISTANCE - 1
			}
			chainTable[off and MASK] = delta.toShort()
			hashTable[h] = off
		}

		override fun previous(off: Int): Int {
			val v = readInt(bytes, off)
			var ref: Int = off - (chainTable[off and MASK].toInt() and 0xFFFF)
			while (ref >= base && attempts < MAX_ATTEMPTS) {
				if (readInt(bytes, ref) == v) {
					return ref
				}
				ref -= chainTable[ref and MASK].toInt() and 0xFFFF
				attempts++
			}
			return -1
		}

		override fun assertReset(): Boolean {
			for (i in chainTable.indices) {
				assert(chainTable[i] == 0xFFFF.toShort()) { i }
			}
			return true
		}

		companion object {
			private const val MAX_ATTEMPTS = 256
			private const val MASK = MAX_DISTANCE - 1
		}


	}
}
