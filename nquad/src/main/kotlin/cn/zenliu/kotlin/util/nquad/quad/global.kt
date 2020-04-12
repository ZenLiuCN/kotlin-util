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
 *   @File: global.kt
 *   @Author:  lcz20@163.com
 *   @LastModified:  2020-04-11 16:23:49
 */

package cn.zenliu.kotlin.util.nquad.quad

import cn.zenliu.kotlin.util.nquad.quad.conversion.*
import cn.zenliu.kotlin.util.nquad.quad.format.*
import cn.zenliu.kotlin.util.nquad.quad.namespace.*
import cn.zenliu.kotlin.util.nquad.quad.schema.*
import cn.zenliu.kotlin.util.nquad.quad.value.*

object Global {
	var defaultIntType = XSD.Integer
		private set
	var defaultDecimalType = XSD.Double
		private set
	var defaultBooleanType = XSD.Boolean
		private set
	var defaultTimeType = XSD.DateTime
		private set

	var formatRegistry: FormatRegistry
		private set
	var namespaceRegistry: NamespaceRegistry<VIRI>
		private set
	var conversionRegistry: ConversionRegistry<VIRI>
		private set

	init {
		namespaceRegistry = NamespaceRegistryImpl(true)
		formatRegistry = FormatRegistryImpl()
		conversionRegistry = ConversionRegistryImpl().apply {
			register(defaultIntType, IntConversion)
			register(defaultBooleanType, BooleanConversion)
			register(defaultDecimalType, DecimalConversion)
			register(defaultTimeType, TimeConversion)
			register(Schema.Integer, IntConversion)
			register(Schema.Boolean, BooleanConversion)
			register(Schema.Float, DecimalConversion)
			register(Schema.Number, DecimalConversion)
			register(Schema.DateTime, TimeConversion)
			register(Schema.Date, TimeConversion)
			register(Schema.Time, TimeConversion)
		}
	}
}


