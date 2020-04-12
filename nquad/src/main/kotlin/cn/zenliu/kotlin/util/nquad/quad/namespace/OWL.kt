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
 *   @File: OWL.kt
 *   @Author:  lcz20@163.com
 *   @LastModified:  2020-04-11 16:23:49
 */

package cn.zenliu.kotlin.util.nquad.quad.namespace

import cn.zenliu.kotlin.util.nquad.quad.*
import cn.zenliu.kotlin.util.nquad.quad.schema.*
import cn.zenliu.kotlin.util.nquad.quad.value.*


object OWL : Namespace {
	init {
		Global.namespaceRegistry.register(this)
	}

	override val prefix = "owl:"
	override val namespace = "http://www.w3.org/2002/07/owl#"

	val UnionOf = VIRI("${prefix}unionOf")
	val Restriction = VIRI("${prefix}Restriction")
	val OnProperty = VIRI("${prefix}onProperty")
	val Cardinality = VIRI("${prefix}cardinality")
	val MaxCardinality = VIRI("${prefix}maxCardinality")
	val Thing = VIRI("${prefix}Thing")
	val Class = VIRI("${prefix}Class")
	val DatatypeProperty = VIRI("${prefix}DatatypeProperty")
	val ObjectProperty = VIRI("${prefix}ObjectProperty")
}



