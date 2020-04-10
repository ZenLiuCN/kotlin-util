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
 *   @Module: format
 *   @File: BinaryFormat.kt
 *   @Author:  lcz20@163.com
 *   @LastModified:  2020-04-11 00:03:55
 */

package cn.zenliu.kotlin.util.format


@Suppress("SpellCheckingInspection")
/**
 * ## HexString
 *  0123456789ABCDEF or 0123456789abcdef
 * ## HexTiny
 * + length is 2/3 of raw string
 * + code in  0123456789abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ-_
 * + only length with times of 3 hexString can be compressed
 * ## HexTinyV2
 * + Char length is half of raw hex string
 * + code in any UNICODE
 * + only length with times of 2, hexString can be compressed
 */
object BinaryFormat {
	private const val H = "0123456789ABCDEF"
	private const val h = "0123456789abcdef"
	private const val code = "0123456789abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ-_"
	val String.toHexTinyV2: String
		get() = buildString {
			val s = this@toHexTinyV2
			(s.indices step 2).forEach { i ->
				append((h.indexOf(s[i]).shl(4) + h.indexOf(s[i + 1])).toChar())
			}
		}
	val String.fromHexTinyV2: String
		get() = fold(StringBuilder()) { b, c ->
			c.toInt().let {
				b.append(h[it.shr(4).and(0xf)])
				b.append(h[it.and(0xf)])
			}
			b
		}.toString()

	fun ByteArray.toHexString(upper: Boolean = false): String = buildString {
		val c = if (upper) H else h
		this@toHexString.forEach {
			val ord = it.toInt()
			/*		val fi = (ord and 0xF0).ushr(4)
					val si = ord and 0x0F*/
			append(c[(ord and 0xF0).ushr(4)])
			append(c[ord and 0x0F])
		}
	}

	fun String.hexToByteArray(): ByteArray {
		val c = this.toLowerCase()
		val len = length
		val result = ByteArray(len / 2)
		(0 until len step 2).forEach { i ->
			result[i.shr(1)] = h.indexOf(c[i]).shl(4).or(h.indexOf(c[i + 1])).toByte()
		}
		return result
	}

	fun String.isHex(): Boolean = toLowerCase().all { h.contains(it) }

	fun String.isHexTiny(): Boolean = takeIf { length.rem(2) == 0 }?.all { code.contains(it) } == true

	/**
	 * padding zero for Hex String Compress
	 * @receiver String
	 * @return String
	 */
	val String.padHexTiny: String
		get() {
			val rem = length.rem(3)
			return if (rem == 0) {
				this
			} else padStart(length + 3 - rem, '0')
		}

	/**
	 * remove padding zero for decompress
	 * @receiver String
	 * @return String
	 */
	val String.unpadHexTiny: String
		get() = fold(StringBuilder()) { b, c ->
			when {
				b.isEmpty() && c == '0' -> Unit
				else -> b.append(c)
			}
			b
		}.toString()

	/**
	 * restore compressed hex string 2 char -> 3 char
	 * @receiver String
	 * @return String
	 */
	val String.toHexTiny: String
		get(): String {
			if (length.rem(2) != 0) {
				throw IllegalArgumentException("$this =>${this.length} rem 2 =${length.rem(2)}")
			}
			return buildString {
				val str = this@toHexTiny.toCharArray()
				for (i in 0..str.lastIndex step 2) {
					val pre = str[i].asInt
					val end = str[i + 1].asInt
					append((pre shr 2).asChar)
					append(((pre and 3 shl 2) + (end shr 4)).asChar)
					append((end and 15).asChar)
				}
			}
		}

	/**
	 *  compress hex string 3 char -> 2 char
	 * @receiver String
	 * @return String
	 */
	val String.fromHexTiny: String
		get() {
			if (length.rem(3) != 0) {
				throw IllegalArgumentException("$this =>${this.length} rem 3 = ${this.length.rem(3)}")
			}
			return buildString {
				val s = this@fromHexTiny
				(s.indices step 3).forEach { i ->
					val pre = s[i].asInt
					val mid = s[i + 1].asInt
					val end = s[i + 2].asInt
					append(((pre shl 2) + (mid shr 2)).asChar)
					append((((mid and 3) shl 4) + end).asChar)
				}
			}
		}

	/**
	 * char is one of the compress char code
	 * @receiver Char
	 * @return Boolean
	 */
	fun Char.isHexTinyCode(): Boolean = code.contains(this)

	private val Int.asChar
		get() = when (this) {
			in 0..63 -> code[this]
			else -> throw IllegalArgumentException()
		}
	private val Char.asInt
		get() = code
			.indexOf(this)
			.let {
				if (it == -1) throw IllegalArgumentException()
				it
			}
}
