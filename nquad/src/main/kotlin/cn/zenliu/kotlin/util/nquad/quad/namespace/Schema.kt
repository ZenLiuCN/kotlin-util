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
 *   @File: Schema.kt
 *   @Author:  lcz20@163.com
 *   @LastModified:  2020-04-11 16:23:49
 */

package cn.zenliu.kotlin.util.nquad.quad.namespace

import cn.zenliu.kotlin.util.nquad.quad.*
import cn.zenliu.kotlin.util.nquad.quad.schema.*
import cn.zenliu.kotlin.util.nquad.quad.value.*

object Schema : Namespace {
	init {
		Global.namespaceRegistry.register(this)
	}

	override val prefix = "schema:"
	override val namespace = "http://schema.org/"


	// The basic data types such as Integers, Strings, etc.
	val DataType = VIRI("${prefix}DataType")

	// Boolean: True or False.
	val Boolean = VIRI("${prefix}Boolean")

	// The boolean value false.
	val False = VIRI("${prefix}False")

	// The boolean value true.
	val True = VIRI("${prefix}True")

	// Data type: Text.
	val Text = VIRI("${prefix}Text")

	// Data type: URL.
	val URL = VIRI("${prefix}URL")

	// Data type: Number.
	val Number = VIRI("${prefix}Number")

	// Data type: Floating number.
	val Float = VIRI("${prefix}Float")

	// Data type: Integer.
	val Integer = VIRI("${prefix}Integer")

	// A date value in ISO 8601 date format.
	val Date = VIRI("${prefix}Date")

	// A point in time recurring on multiple days in the form hh:mm:ss[Z|(+|-)hh:mm].
	val Time = VIRI("${prefix}Time")

	// A combination of date and time of day in the form [-]CCYY-MM-DDThh:mm:ss[Z|(+|-)hh:mm] (see Chapter 5.4 of ISO 8601).
	val DateTime = VIRI("${prefix}DateTime")

	// A class, also often called a 'Type'; equivalent to rdfs:Class.
	val Class = VIRI("${prefix}Class")

	// A property, used to indicate attributes and relationships of some Thing; equivalent to rdf:Property.
	val Property = VIRI("${prefix}Property")

	val Name = VIRI("${prefix}name")
	val UrlProp = VIRI("${prefix}url")
}
