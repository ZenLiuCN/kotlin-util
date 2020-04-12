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
 *   @File: RDF.kt
 *   @Author:  lcz20@163.com
 *   @LastModified:  2020-04-11 16:23:49
 */

package cn.zenliu.kotlin.util.nquad.quad.namespace

import cn.zenliu.kotlin.util.nquad.quad.*
import cn.zenliu.kotlin.util.nquad.quad.schema.*
import cn.zenliu.kotlin.util.nquad.quad.value.*

object RDF : Namespace {
	init {
		Global.namespaceRegistry.register(this)
	}

	override val prefix = "rdf:"
	override val namespace = "http://www.w3.org/1999/02/22-rdf-syntax-ns#"

	// Types

	// The datatype of RDF literals storing fragments of HTML content
	val HTML = VIRI("${prefix}HTML")

	// The datatype of language-tagged string values
	val LangString = VIRI("${prefix}langString")

	// The class of plain (i.e. untyped) literal values, as used in RIF and OWL 2
	val PlainLiteral = VIRI("${prefix}PlainLiteral")

	// The class of RDF properties.
	val Property = VIRI("${prefix}Property")

	// The class of RDF statements.
	val Statement = VIRI("${prefix}Statement")

	// Properties

	// The subject is an instance of a class.
	val Type = VIRI("${prefix}type")

	// Idiomatic property used for structured values.
	val Value = VIRI("${prefix}value")

	// The subject of the subject RDF statement.
	val Subject = VIRI("${prefix}subject")

	// The predicate of the subject RDF statement.
	val Predicate = VIRI("${prefix}predicate")

	// The object of the subject RDF statement.
	val Object = VIRI("${prefix}object")

	// The class of unordered containers.
	val Bag = VIRI("${prefix}Bag")

	// The class of ordered containers.
	val Seq = VIRI("${prefix}Seq")

	// The class of containers of alternatives.
	val Alt = VIRI("${prefix}Alt")

	// The class of RDF Lists.
	val List = VIRI("${prefix}List")

	// The empty list, with no items in it. If the rest of a list is nil then the list has no more items in it.
	val Nil = VIRI("${prefix}nil")

	// The first item in the subject RDF list.
	val First = VIRI("${prefix}first")

	// The rest of the subject RDF list after the first item.
	val Rest = VIRI("${prefix}rest")

	// The datatype of XML literal values.
	val XMLLiteral = VIRI("${prefix}XMLLiteral")
}
