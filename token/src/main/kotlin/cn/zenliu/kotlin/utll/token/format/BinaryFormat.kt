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
 *   @Module: token
 *   @File: BinaryFormat.kt
 *   @Author:  lcz20@163.com
 *   @LastModified:  2020-04-10 23:20:13
 */

package cn.zenliu.kotlin.utll.token.format

@Suppress("SpellCheckingInspection")
object BinaryFormat {
	private const val H = "0123456789ABCDEF"
	private const val h = "0123456789abcdef"
	private const val code = "0123456789abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ-_"
	fun ByteArray.toHex(upper: Boolean = false): String = buildString {
		val c = if (upper) H else h
		this@toHex.forEach {
			val ord = it.toInt()
			/*		val fi = (ord and 0xF0).ushr(4)
					val si = ord and 0x0F*/
			append(c[(ord and 0xF0).ushr(4)])
			append(c[ord and 0x0F])
		}
	}

	fun String.fromHex(): ByteArray {
		val c = this.toLowerCase()
		val len = length
		val result = ByteArray(len / 2)
		(0 until len step 2).forEach { i ->
			result[i.shr(1)] = h.indexOf(c[i]).shl(4).or(h.indexOf(c[i + 1])).toByte()
		}
		return result
	}

	fun String.isHexString() = toLowerCase().all { h.contains(it) }

	fun String.isCompressedHexString() = all { code.contains(it) }

	/**
	 * padding zero for Hex String Compress
	 * @receiver String
	 * @return String
	 */
	fun String.padHex() = run {
		val rem = length.rem(3)
		if (rem == 0) {
			this
		} else padStart(length + 3 - rem, '0')
	}

	/**
	 * remove padding zero for decompress
	 * @receiver String
	 * @return String
	 */
	fun String.padHexRestore() = fold(StringBuilder()) { b, c ->
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
	fun String.compressedHexRestore(): String {
		if (length.rem(2) != 0) {
			throw IllegalArgumentException("$this =>${this.length} rem 2 =${length.rem(2)}")
		}
		return buildString {
			val str = this@compressedHexRestore.toCharArray()
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
	fun String.hexCompress(): String {
		if (length.rem(3) != 0) {
			throw IllegalArgumentException("$this =>${this.length} rem 3 = ${this.length.rem(3)}")
		}
		return buildString {
			val str = this@hexCompress.toCharArray()
			(0..str.lastIndex step 3).forEach { i ->
				val pre = str[i].asInt
				val mid = str[i + 1].asInt
				val end = str[i + 2].asInt
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
	fun Char.isCompressedChar() = code.contains(this)

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
