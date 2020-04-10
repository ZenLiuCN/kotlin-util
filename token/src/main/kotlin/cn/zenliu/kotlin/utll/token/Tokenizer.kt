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
 *   @File: Tokenizer.kt
 *   @Author:  lcz20@163.com
 *   @LastModified:  2020-04-06 18:47:11
 */

package cn.zenliu.kotlin.utll.token

import cn.zenliu.bsonid.BsonShortId
import java.security.SecureRandom
import kotlin.reflect.KClass

typealias Formula = List<KClass<*>>

object Tokenizer {

	//<editor-fold desc="Hex Compressor">
	internal fun decompress(shortId: String?): String {
		if (shortId == null || shortId.length.rem(2) != 0) {
			throw IllegalArgumentException("$shortId =>${shortId?.length} =>${shortId?.length?.rem(2)}")
		}
		return buildString {
			val str = shortId.replace(mapperTo, mapperFrom).toCharArray()
			for (i in 0..str.lastIndex step 2) {
				val pre = str[i].asInt
				val end = str[i + 1].asInt
				append((pre shr 2).asChar)
				append(((pre and 3 shl 2) + (end shr 4)).asChar)
				append((end and 15).asChar)
			}
		}
	}

	internal fun compress(longId: String?): String {
		if (longId == null || longId.length.rem(3) != 0) {
			throw IllegalArgumentException("$longId =>${longId?.length} =>${longId?.length?.rem(3)}")
		}
		return buildString {
			val str = longId.toCharArray()
			for (i in 0..str.lastIndex step 3) {
				val pre = str[i].asInt
				val mid = str[i + 1].asInt
				val end = str[i + 2].asInt
				append(((pre shl 2) + (mid shr 2)).asChar)
				append((((mid and 3) shl 4) + end).asChar)
			}
		}.replace(mapperFrom, mapperTo)
	}

	private var mapperTo: Char = '.'

	/**
	 * also use as Part connector
	 */
	var mapperFrom: Char = '-'
		private set

	fun setMapperChar(from: Char?, to: Char?) {
		assert(
			when {
				from == to -> false
				from != null && code.contains(from) -> false
				to != null && code.contains(to) -> false
				else -> true
			}
		)
		{ "from<$from> and to<$to> must both null or both NOT null or in [0-9a-Z\\-\\_]" }
		mapperFrom = from ?: '-'
		mapperTo = to ?: '.'
	}


	@Suppress("SpellCheckingInspection")
	private const val code = "0123456789abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ-_"
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
	//</editor-fold>

	internal fun rand() = SecureRandom().nextLong().let { if (it < 0) -it else it }

	/**
	 * what class Tokenizer formula supported
	 */
	val supportedClass = listOf(
		Int::class,
		Byte::class,
		String::class,
		Long::class)

	private fun Formula.assert() = run {
		assert(all { supportedClass.contains(it) }) { "none supported class ${find { !supportedClass.contains(it) }?.simpleName} in formula" }
		this
	}

	private fun Formula.validate() =
		takeIf { it.isNotEmpty() }
			.runCatching { formula.assert() }
			.getOrNull() != null

	/**
	 * check is formula valid
	 * @param formula List<KClass<*>>
	 * @return Boolean
	 */
	fun checkFormula(formula: Formula): Boolean = kotlin
		.runCatching { formula.assert() }
		.getOrNull() != null


	private val BsonShortId.hashCoding
		get() = instant.epochSecond.and(0xff) + counter

	private var formula: Formula = listOf(Long::class)

	/**
	 * set default formula
	 * @param formula List<KClass<*>>
	 */
	fun setFormula(formula: Formula) {
		this.formula = formula.assert()
	}

	/**
	 * String will be decompress as LZ Base64 compressed string
	 * @receiver String
	 * @return String?
	 */
	private fun String.restoreString(): String? = this
		.split('0')
		.filter { it.isNotBlank() }
		.joinToString("") {
			it.padStart(4, '0')
		}

	// LZ4K.decompressFromBase64(this)
	private fun String.reduceString() = this
		.chunked(4)
		.joinToString("") {
			"0${it.replaceFirst("^0+".toRegex(), "")}"
		}
	//LZ4K.compressToBase64(this).replace("=", "")


	private fun String.padForCompress(count: Int = 3) = run {
		val rem = length.rem(count)
		if (rem == 0) {
			this
		} else padStart(length + count - rem, '0')
	}

	private fun String.decode(kClass: KClass<*>?, id: BsonShortId) = decompress(
		if (kClass == String::class) {
			this.restoreString()
		} else this
	).let { code ->
		when (kClass) {
			null -> null
			Long::class -> code.toLongOrNull(16)?.let { it - id.hashCoding }
			Int::class -> code.toIntOrNull(16)?.let { it - id.hashCoding }
			Byte::class -> code.toByteOrNull(16)?.let { it - id.hashCoding }
			String::class -> code.chunked(6).map {
				it.toLongOrNull(16)?.toChar()
					?: throw IllegalArgumentException("fail to decode Char from '$it'")
			}.joinToString("") { it.toString() }
			else -> null
		}
	}

	private fun Any.encode(id: BsonShortId) = when (this) {
		is Int -> (this + id.hashCoding).toString(16).padForCompress().let(::compress)
		is Long -> (this + id.hashCoding).toString(16).padForCompress().let(::compress)
		is Byte -> (this + id.hashCoding).toString(16).padForCompress().let(::compress)
		is String -> this.toCharArray().joinToString("") {
			it.toLong().toString(16).padStart(6, '0')
		}.let(::compress).reduceString()
		else -> throw IllegalArgumentException("fail to encode type of ${this::class.simpleName} of $this")
	}

	/**
	 *
	 * @param payload List<Any>
	 * @param formula List<KClass<*>>? optional default use pre set formula
	 * @return String?
	 */
	fun generator(payload: List<Any>, formula: Formula? = null): String? = run {
		val form = (formula ?: this.formula)
		if (payload.size != form.size) return@run null
		val bsTK = BsonShortId.get()
		kotlin.runCatching {
			buildString {
				payload.forEachIndexed { i, c ->
					if (i > 0) append(mapperFrom)
					append(c.encode(bsTK))
				}
				append(mapperFrom)
				append(bsTK.hex.replace(mapperFrom, mapperTo))
			}
		}.onFailure {
			println(it)
		}.getOrNull()
	}

	/**
	 *
	 * @param token String
	 * @param formula List<KClass<*>>? optional default use pre set formula
	 * @return List<Serializable>? null(token invalid) List<Formula,BsonShortId>
	 */
	fun parser(token: String, formula: Formula? = null): List<Any>? =
		(formula ?: this.formula)
			.let { form ->
				token
					//not blank string and match formula size(have divider count eq formula length)
					.takeIf {
						it.isNotBlank()
							&& form.validate()
							&& it.count { c -> c == mapperFrom } == form.size
					}
					?.split(mapperFrom)
					?.let parse@{
						// last one must be bsonShortId
						val bsTK = kotlin.runCatching {
								BsonShortId(it.last().replace(mapperTo, mapperFrom))
							}
							.getOrNull()
							?: return@parse null
						kotlin.runCatching {
							it.mapIndexedNotNull { i, s ->
									if (i == it.lastIndex) null
									else s.decode(form[i], bsTK)
								}
								.toMutableList()
								.apply { add(bsTK) }
						}.onFailure {
							println(it)
						}.getOrNull()
					}
			}

}
