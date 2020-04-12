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
 *   @File: LZ4V2.kt
 *   @Author:  lcz20@163.com
 *   @LastModified:  2020-04-11 23:14:56
 */

package cn.zenliu.kotlin.util.compress


object LZ4V2 {
	private const val mlBits = 4
	private const val mlMask = (1 shl mlBits) - 1
	private const val runBits = 8 - mlBits
	private const val runMask = (1 shl runBits) - 1
	private const val MaxInputSize = 0x7E000000
	private fun ByteArray.readLEInt(idx: Int = 0) = (3 downTo 0)
		.sumBy { this[it + idx].toInt() and 255 shl (8 * it) }

	private fun ByteArray.writeLEInt(i: Int, idx: Int = 0) {
		this[idx] = (i ushr 0 and 0xFF).toByte()
		this[idx + 1] = (i ushr 8 and 0xFF).toByte()
		this[idx + 2] = (i ushr 16 and 0xFF).toByte()
		this[idx + 3] = (i ushr 24 and 0xFF).toByte()
	}

	private fun Array<Byte>.writeLEInt(i: Int, idx: Int = 0) {
		this[idx] = (i ushr 0 and 0xFF).toByte()
		this[idx + 1] = (i ushr 8 and 0xFF).toByte()
		this[idx + 2] = (i ushr 16 and 0xFF).toByte()
		this[idx + 3] = (i ushr 24 and 0xFF).toByte()
	}

	//region Decode
	private class Decoder(
		val src: ByteArray,
		val dst: Array<Byte>,
		var spos: Int,
		var dpos: Int,
		var ref: Int
	) {
		fun readByte() = runCatching {
			src[spos].also { _ ->
				spos++
			}
		}.getOrNull()

		fun len() = run {
			var len = 0
			var ln = readByte()?.toInt() ?: 0
			while (ln == 255) {
				len += 255
				ln = readByte()?.toInt() ?: 0
			}
			len + ln
		}

		fun cp(len: Int, des: Int) {
			if (ref + len < dpos) {
				dst.copyInto(dst, dpos, ref, ref + len)
			} else
				(0 until len).forEach { i ->
					dst[dpos + i] = dst[ref + i]
				}
			dpos += len
			ref += len - des
		}
	}

	fun decode(src: ByteArray): ByteArray? {
		if (src.size < 4) return null
		val srcLen = src.size
		val unLen = src.readLEInt()
		if (unLen == 0) return null
		if (unLen > MaxInputSize) return null
		val dst = Array<Byte>(unLen) { 0 }
		val d = Decoder(src, dst, 4, 0, 0)
		val decr = intArrayOf(0, 3, 2, 3)
		var code = 0
		while (d.readByte()?.also { code = it.toInt() } != null) {
			var len = code.shr(mlBits)
			if (len == runMask) {
				len += d.len()//if is zero?
			}
			if (d.spos + len > srcLen || d.dpos + len > dst.size) {
				return null
			}
			(0 until len).forEach {
				d.dst[d.dpos + it] = d.src[d.spos + it]
			}
			d.spos += len
			d.dpos += len
			if (d.spos == srcLen) {
				return d.dst.toByteArray()
			}
			if (d.spos + 2 >= srcLen) {
				return null
			}
			val back = d.src[d.spos].toInt().or(d.src[d.spos + 1].toInt())
			if (back > d.dpos) {
				return null
			}
			d.spos += 2
			d.ref = d.dpos + back
			len = code.and(mlMask)
			if (len == mlMask) {
				len += d.len()//if is zero?
			}
			val literal = d.dpos - d.ref
			if (literal < 4) {
				if (d.dpos + 4 > d.dst.size) {
					return null
				}
				d.cp(4, decr[literal])
			} else {
				len += 4
			}
			if (d.dpos + len > unLen) {
				return null
			}

			d.cp(len, 0)
		}
		return d.dst.toByteArray()
	}
	//endregion

	//<editor-fold desc="Encode">
	private const val minMatch = 4
	private const val hashLog = 17
	private const val hashTableSize = 1 shl hashLog
	private const val hashShift = (minMatch * 8) - hashLog
	private const val incompressible = 128
	private const val uninitHash = 0x88888888

	private class Encoder(
		val src: ByteArray,
		val dst: Array<Byte>,
		val hashTable: MutableMap<Long, Long>,
		var pos: Int,
		var anchor: Int,
		var dpos: Int
	) {
		fun writeLiterals(length: Int, mlLen: Int, pos: Int) {
			var ln = length
			var code: Int = if (ln > runMask - 1) {
				runMask
			} else {
				ln
			}

			if (mlLen > mlMask - 1) {
				dst[dpos] = ((code shl mlBits) + mlMask).toByte()
			} else {
				dst[dpos] = ((code shl mlBits) + mlLen).toByte()
			}
			dpos++

			if (code == runMask) {
				ln -= runMask
				while (ln > 254) {
					dst[dpos] = 255.toByte()
					dpos++
					ln -= 255
				}
				dst[dpos] = ln.toByte()
				dpos++
			}

			(0 until length).forEach {
				dst[dpos + it] = src[pos + it]
			}
			dpos += length
		}

		fun output() = dst.sliceArray(0..dpos).toByteArray()
		fun sequence() =
			src[pos + 3].toInt() shl 24 or src[pos + 2].toInt() shl 16 or src[pos + 1].toInt() shl 8 or src[pos + 0].toInt()
	}

	// CompressBound returns the maximum length of a lz4 block, given it's uncompressed length
	private fun compressBound(isize: Int): Int {
		if (isize > MaxInputSize) {
			return 0
		}
		return isize + ((isize) / 255) + 16 + 4
	}

	fun encode(src: ByteArray): ByteArray? {
		val srcLen = src.size
		if (srcLen > MaxInputSize) {
			return null
		}
		val n = compressBound(srcLen)
		val dst = Array<Byte>(n) { 0 }
		dst.writeLEInt(srcLen)
		val e = Encoder(src, dst, mutableMapOf(), 0, 0, 4)
		var step = 1
		var limit = incompressible
		while (true) {
			if (e.pos + 12 >= srcLen) {
				e.writeLiterals(srcLen - e.anchor, 0, e.anchor)
				return e.output()
			}
			val sequence = e.sequence()
			val hash = (sequence * 2654435761) shr hashShift
			var ref = ((e.hashTable[hash] ?: 0) + uninitHash).toInt()
			e.hashTable[hash] = e.pos - uninitHash

			if (((e.pos - ref) shr 16) != 0
				|| (e.src[ref + 3].toInt() shl 24 or e.src[ref + 2].toInt() shl 16 or e.src[ref + 1].toInt() shl 8 or e.src[ref + 0].toInt()) != sequence) {
				if (e.pos - e.anchor > limit) {
					limit = limit shl 1
					step += 1 + (step shr 2)
				}
				e.pos += step
				continue
			}
			if (step > 1) {
				e.hashTable[hash] = ref - uninitHash
				e.pos -= step - 1
				step = 1
				continue
			}
			limit = incompressible

			val ln = e.pos - e.anchor
			val back = e.pos - ref
			val anchor = e.anchor

			e.pos += minMatch
			ref += minMatch
			e.anchor = e.pos
			while (e.pos < srcLen - 5 && e.src[e.pos] == e.src[ref]) {
				e.pos++
				ref++
			}
			var mlLen = e.pos - e.anchor
			e.writeLiterals(ln, mlLen, anchor)
			e.dst[e.dpos] = back.and(0xff).toByte()
			e.dst[e.dpos + 1] = back.shr(8).and(0xff).toByte()
			e.dpos += 2
			if (mlLen > mlMask - 1) {
				mlLen -= mlMask
				while (mlLen > 254) {
					mlLen -= 255
					e.dst[e.dpos] = 255.toByte()
					e.dpos++
				}
				e.dst[e.dpos] = mlLen.toByte()
				e.dpos++
			}
			e.anchor = e.pos
		}
	}
	//</editor-fold>
}

