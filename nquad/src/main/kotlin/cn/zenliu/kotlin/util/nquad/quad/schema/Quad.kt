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
 *   @Module: nquad
 *   @File: Quad.kt
 *   @Author:  lcz20@163.com
 *   @LastModified:  2020-04-11 16:23:49
 */

package cn.zenliu.kotlin.util.nquad.quad.schema

import cn.zenliu.kotlin.util.nquad.quad.*


/**
 *  ## Quad is extend RDF struct
 *
 * @param S : Any
 * @param P : Any
 * @param O : Any
 * @param T : Any
 * @property sub S
 * @property pre P
 * @property obj O
 * @property tag T?
 */
interface Quad<out S : Value<*>> {
	val sub: S
	val pre: S
	val obj: S
	val tag: S?

	/**
	 * get the nQuad in string
	 * @return String
	 */
	fun nQuad(): String = buildString {
		append(sub)
		append(QUAD_SP)
		append(pre)
		append(QUAD_SP)
		append(obj)
		tag?.apply {
			append(QUAD_SP)
			append(tag)
		}
	}

	/**
	 * get value at direction
	 * @param dir Direction
	 * @return S?
	 */
	fun ofDirection(dir: Direction) = when (dir) {
		Direction.Subject -> sub
		Direction.Predicate -> pre
		Direction.Object -> obj
		Direction.Tag -> tag
		else -> throw QuadIllegalDirection(dir)
	}

	//region Exceptions
	abstract class QuadException(message: String?, cause: Throwable? = null) : Throwable(message, cause)
	class QuadIllegalDirection(dir: Direction) : QuadException("$dir is not allowed")
	//endregion

	//region Enumerations
	/**
	 * direction of nQuad
	 * @property prefix Char
	 * @constructor
	 */
	enum class Direction(private val prefix: Char) {
		UNK(0x00.toChar()),
		Any('a'),
		Subject('s'),
		Predicate('p'),
		Object('o'),
		Tag('l');

		override fun toString(): String = name.toLowerCase()
		fun rawString() = if (name == "UNK") "00" else "quad.$name"
	}
	//endregion

}


