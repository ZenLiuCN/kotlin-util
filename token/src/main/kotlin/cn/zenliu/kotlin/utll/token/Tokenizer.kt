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
 *   @LastModified:  2020-04-10 23:21:39
 */

package cn.zenliu.kotlin.utll.token

import cn.zenliu.bsonid.*
import cn.zenliu.kotlin.utll.token.format.BinaryFormat.compressedHexRestore
import cn.zenliu.kotlin.utll.token.format.BinaryFormat.fromHex
import cn.zenliu.kotlin.utll.token.format.BinaryFormat.hexCompress
import cn.zenliu.kotlin.utll.token.format.BinaryFormat.isCompressedChar
import cn.zenliu.kotlin.utll.token.format.BinaryFormat.padHex
import cn.zenliu.kotlin.utll.token.format.BinaryFormat.padHexRestore
import cn.zenliu.kotlin.utll.token.format.BinaryFormat.toHex
import java.security.*
import kotlin.reflect.*

typealias Formula = List<KClass<*>>

object Tokenizer {

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
				from != null && from.isCompressedChar() -> false
				to != null && to.isCompressedChar() -> false
				else -> true
			}
		)
		{ "from<$from> and to<$to> must both null or both NOT null or in [0-9a-Z\\-\\_]" }
		mapperFrom = from ?: '-'
		mapperTo = to ?: '.'
	}

	internal fun rand() = SecureRandom().nextLong().let { if (it < 0) -it else it }

	//<editor-fold desc="Formula">
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

	private var formula: Formula = listOf(Long::class)

	/**
	 * set default formula
	 * @param formula List<KClass<*>>
	 */
	fun setFormula(formula: Formula) {
		this.formula = formula.assert()
	}
	//</editor-fold>

	private val BsonShortId.hashCoding
		get() = instant.epochSecond.and(0xff) + counter


	private fun String.decode(kClass: KClass<*>?, id: BsonShortId) =
		this.replace(mapperTo, mapperFrom).compressedHexRestore()
			.let { code ->
				when (kClass) {
					null -> null
					Long::class -> code.toLongOrNull(16)?.let { it - id.hashCoding }
					Int::class -> code.toIntOrNull(16)?.let { it - id.hashCoding }
					Byte::class -> code.toByteOrNull(16)?.let { it - id.hashCoding }
					String::class -> code.padHexRestore().fromHex().let { String(it) }
					else -> null
				}
			}

	private fun Any.encode(id: BsonShortId) = when (this) {
		is Int -> (this + id.hashCoding).toString(16)
		is Long -> (this + id.hashCoding).toString(16)
		is Byte -> (this + id.hashCoding).toString(16)
		is String -> toByteArray().toHex()
		else -> throw IllegalArgumentException("fail to encode type of ${this::class.simpleName} of $this")
	}.padHex().hexCompress().replace(mapperFrom, mapperTo)

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
