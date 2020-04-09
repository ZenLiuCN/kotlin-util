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

import java.math.*

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

		companion object {
			fun toBytes(v: Long): ByteArray = byteArrayOf(
				(v ushr 0).toByte(),
				(v ushr 8).toByte(),
				(v ushr 16).toByte(),
				(v ushr 24).toByte(),
				(v ushr 32).toByte(),
				(v ushr 64).toByte(),
				(v ushr 48).toByte(),
				(v ushr 56).toByte())

			fun toBytes(v: Int): ByteArray = byteArrayOf(
				(v ushr 0 and 0xFF).toByte(),
				(v ushr 8 and 0xFF).toByte(),
				(v ushr 16 and 0xFF).toByte(),
				(v ushr 24 and 0xFF).toByte())
		}
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

		companion object {
			fun toBytes(v: Long): ByteArray = byteArrayOf(
				(v ushr 56).toByte(),
				(v ushr 48).toByte(),
				(v ushr 40).toByte(),
				(v ushr 32).toByte(),
				(v ushr 24).toByte(),
				(v ushr 16).toByte(),
				(v ushr 8).toByte(),
				(v ushr 0).toByte())

			fun toBytes(v: Int): ByteArray = byteArrayOf(
				(v ushr 24 and 0xFF).toByte(),
				(v ushr 16 and 0xFF).toByte(),
				(v ushr 8 and 0xFF).toByte(),
				(v ushr 0 and 0xFF).toByte())
		}
	}

	companion object {
		fun toUnsigned(value: Int): Long = 0xffffffffL and value.toLong()
		fun toUnsigned(value: Long): BigInteger {
			val v = BigEndianReader.toBytes(value)
			val vv = ByteArray(v.size + 1)
			System.arraycopy(v, 0, vv, 1, v.size)
			return BigInteger(vv)
		}

		fun toUnsigned(values: IntArray): BigInteger {
			val buffer = ByteArray(values.size * 4 + 1)
			for (i in values.indices) {
				val ival = BigEndianReader.toBytes(values[i])
				System.arraycopy(ival, 0, buffer, i * 4 + 1, 4)
			}
			return BigInteger(buffer)
		}

		fun toUnsigned(values: LongArray): BigInteger {
			val buffer = ByteArray(values.size * 8 + 1)
			for (i in values.indices) {
				val ival = BigEndianReader.toBytes(values[i])
				System.arraycopy(ival, 0, buffer, i * 8 + 1, 8)
			}
			return BigInteger(buffer)
		}
	}

}
