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
 *   @File: ConversionRegistryImpl.kt
 *   @Author:  lcz20@163.com
 *   @LastModified:  2020-04-11 16:23:49
 */

package cn.zenliu.kotlin.util.nquad.quad.conversion

import cn.zenliu.kotlin.util.nquad.quad.schema.*
import cn.zenliu.kotlin.util.nquad.quad.value.*
import java.math.*
import java.time.*

class ConversionRegistryImpl(
	override val registry: MutableMap<VIRI, Conversion<*>> = mutableMapOf()
) : ConversionRegistry<VIRI> {
	override fun register(iri: VIRI, converter: Conversion<*>) {
		registry[iri.toShort()] = converter
		registry[iri.toFull()] = converter
	}
}

object IntConversion : Conversion<Int> {
	override fun invoke(raw: String): Value<Int> = VInt(raw.toInt())
}

object BooleanConversion : Conversion<Boolean> {
	override fun invoke(raw: String): Value<Boolean> = VBoolean(raw.toBoolean())
}

object DecimalConversion : Conversion<BigDecimal> {
	override fun invoke(raw: String): Value<BigDecimal> = VDecimal(raw.toBigDecimal())
}

/**
 * note use timestamp of epoch mill to transmit time value
 */
object TimeConversion : Conversion<Instant> {
	override fun invoke(raw: String): Value<Instant> = VTime(Instant.ofEpochMilli(raw.toLong()))
}
