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
 *   @File: XSD.kt
 *   @Author:  lcz20@163.com
 *   @LastModified:  2020-04-11 16:23:49
 */

package cn.zenliu.kotlin.util.nquad.quad.namespace

import cn.zenliu.kotlin.util.nquad.quad.*
import cn.zenliu.kotlin.util.nquad.quad.schema.*
import cn.zenliu.kotlin.util.nquad.quad.value.*

object XSD : Namespace {
	init {
		Global.namespaceRegistry.register(this)
	}

	override val prefix = "xsd:"
	override val namespace = "http://www.w3.org/2001/XMLSchema#"

	// Boolean represents the values of two-valued logic.
	val Boolean = VIRI("${prefix}boolean")

	// String represents character strings
	val String = VIRI("${prefix}string")

	// Double datatype is patterned after the IEEE double-precision 64-bit floating point datatype [IEEE 754-2008]. Each floating point datatype has a value space that is a subset of the rational numbers.  Floating point numbers are often used to approximate arbitrary real numbers.
	val Double = VIRI("${prefix}double")

	// DateTime represents instants of time, optionally marked with a particular time zone offset.  Values representing the same instant but having different time zone offsets are equal but not identical.
	val DateTime = VIRI("${prefix}dateTime")

	// Integer is derived from decimal by fixing the value of fractionDigits to be 0 and disallowing the trailing decimal point. This results in the standard mathematical concept of the integer numbers.
	val Integer = VIRI("${prefix}integer")

	// Long is derived from integer by setting the value of maxInclusive to be 9223372036854775807 and minInclusive to be -9223372036854775808. The base type of long is integer.
	val Long = VIRI("${prefix}long")

	// Int is derived from long by setting the value of maxInclusive to be 2147483647 and minInclusive to be -2147483648.
	val Int = VIRI("${prefix}int")

	// Float datatype is patterned after the IEEE single-precision 32-bit floating point datatype
	val Float = VIRI("${prefix}float")

}
