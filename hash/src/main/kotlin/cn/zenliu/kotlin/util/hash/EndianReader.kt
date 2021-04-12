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
 *   @Module: hash
 *   @File: EndianReader.kt
 *   @Author:  lcz20@163.com
 *   @LastModified:  2020-04-09 21:22:13
 */

package cn.zenliu.kotlin.util.hash

import java.math.BigInteger


interface EndianReader {
	val size: Int
	operator fun get(i: Int): Byte

	fun getIntOrNull(i: Int): Int?
	fun getLongOrNull(i: Int): Long?

	fun getInt(i: Int): Int = getIntOrNull(i) ?: throw ArrayIndexOutOfBoundsException(i)
	fun getLong(i: Int): Long = getLongOrNull(i) ?: throw ArrayIndexOutOfBoundsException(i)

	class LittleEndianReader(private val bytes: ByteArray) : EndianReader {
		override val size: Int get() = bytes.size
		override operator fun get(i: Int): Byte = bytes[i]
		override fun getIntOrNull(i: Int): Int? = runCatching {
			(3 downTo 0)
				.sumBy { bytes[it + i].toInt() and 255 shl (8 * it) }
		}.getOrNull()

		override fun getLongOrNull(i: Int): Long? = runCatching {
			(7 downTo 0)
				.map { bytes[it + i].toLong() and 255.toLong() shl (8 * it) }
				.sum()
		}.getOrNull()


	}

	class BigEndianReader(private val bytes: ByteArray) : EndianReader {
		override val size: Int get() = bytes.size
		override operator fun get(i: Int) = bytes[i]
		override fun getIntOrNull(i: Int): Int? = runCatching {
			(0..3)
				.sumBy { bytes[it + i].toInt() and 255 shl 8 * (3 - it) }
		}.getOrNull()

		override fun getLongOrNull(i: Int): Long? = runCatching {
			(0..7)
				.map { bytes[it].toLong() and 255.toLong() shl (8 * (7 - it)) }
				.sum()
		}.getOrNull()


	}

	companion object {
		fun toUnsigned(value: Int): Long = 0xffffffffL and value.toLong()
		fun toUnsigned(value: Long): BigInteger {
			toBigEndianBytes(value).let { v ->
				val vv = ByteArray(v.size + 1)
				v.copyInto(vv, 1, 0, v.size)
				return BigInteger(vv)
			}
		}

		fun toUnsigned(values: IntArray): BigInteger {
			val buffer = ByteArray(values.size * 4 + 1)
			values.indices.forEach { i ->
				toBigEndianBytes(values[i]).copyInto(buffer, i * 4 + 1, 0, 4)
			}
			return BigInteger(buffer)
		}

		fun toUnsigned(values: LongArray): BigInteger {
			val buffer = ByteArray(values.size * 8 + 1)
			values.indices.forEach { i ->
				toBigEndianBytes(values[i]).copyInto(buffer, i * 8 + 1, 0, 8)
			}
			return BigInteger(buffer)
		}

		fun longFromLittleEndian(bytes: ByteArray, off: Int = 0, length: Int = bytes.size): Long {
			var l: Long = 0
			(0 until length).forEach { i ->
				l = l or (bytes[off + i].toLong() and 0xffL shl 8 * i)
			}
			return l
		}

		fun longFromLittleEndianV2(bytes: ByteArray, off: Int = 0): Long {
			var l: Long = 0
			(0 until 7).forEach { i ->
				l = l or (bytes[off + i].toLong() and 0xffL shl 8 * i)
			}
			return l
		}

		fun intFromLittleEndian(bytes: ByteArray, off: Int = 0): Int {
			return longFromLittleEndian(bytes, off, 4).toInt()
		}

		fun intFromLittleEndianV2(bytes: ByteArray, off: Int = 0): Int {
			var l: Int = 0
			(0 until 4).forEach { i ->
				l = l or (bytes[off + i].toInt() and 0xff shl 8 * i)
			}
			return l
		}

		fun toBigEndianBytes(v: Long): ByteArray = byteArrayOf(
			(v ushr 56).toByte(),
			(v ushr 48).toByte(),
			(v ushr 40).toByte(),
			(v ushr 32).toByte(),
			(v ushr 24).toByte(),
			(v ushr 16).toByte(),
			(v ushr 8).toByte(),
			(v ushr 0).toByte())

		fun toBigEndianBytes(v: Int): ByteArray = byteArrayOf(
			(v ushr 24 and 0xFF).toByte(),
			(v ushr 16 and 0xFF).toByte(),
			(v ushr 8 and 0xFF).toByte(),
			(v ushr 0 and 0xFF).toByte())

		fun toLittleEndianBytes(v: Long): ByteArray = byteArrayOf(
			(v ushr 0).toByte(),
			(v ushr 8).toByte(),
			(v ushr 16).toByte(),
			(v ushr 24).toByte(),
			(v ushr 32).toByte(),
			(v ushr 40).toByte(),
			(v ushr 48).toByte(),
			(v ushr 56).toByte())

		fun toLittleEndianBytes(v: Int): ByteArray = byteArrayOf(
			(v ushr 0 and 0xFF).toByte(),
			(v ushr 8 and 0xFF).toByte(),
			(v ushr 16 and 0xFF).toByte(),
			(v ushr 24 and 0xFF).toByte())

		fun toLittleEndian(out: ByteArray, value: Long, off: Int, length: Int) {
			var num = value
			(0 until length).forEach { i ->
				out[off + i] = (num and 0xff).toByte()
				num = num shr 8
			}
		}

		fun toBigEndian(out: ByteArray, value: Long, off: Int, length: Int) {
			val last = off + length - 1
			var num = value
			(0 until length).forEach { i ->
				out[last - i] = (num and 0xff).toByte()
				num = num shr 8
			}
		}
	}
}


