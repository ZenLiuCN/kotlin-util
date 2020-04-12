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
 *   @File: util.kt
 *   @Author:  lcz20@163.com
 *   @LastModified:  2020-04-11 16:46:20
 */
@file:JvmName("QuadUtil")

package cn.zenliu.kotlin.util.nquad.quad

import cn.zenliu.kotlin.util.nquad.quad.value.*
import java.math.*
import java.sql.*
import java.time.*


fun String.toQuadString(): VString = VString(this)


fun Int.toQuadInt(): VInt = VInt(this)
fun Boolean.toQuadValue(): VBoolean = VBoolean(this)

fun Float.toQuadValue(): VDecimal = VDecimal(this.toBigDecimal())
fun Double.toQuadValue(): VDecimal = VDecimal(this.toBigDecimal())
fun String.toQuadDecimal(): VDecimal = VDecimal(this.toBigDecimal())
fun BigDecimal.toQuadValue(): VDecimal = VDecimal(this)
fun Long.toQuadValue(): VDecimal = VDecimal(this.toBigDecimal())
fun BigInteger.toQuadValue(): VDecimal = VDecimal(this.toBigDecimal())

fun Long.toQuadTime(): VTime = VTime(Instant.ofEpochMilli(this))
fun Instant.toQuadTime(): VTime = VTime(this)
fun Timestamp.toQuadTime(): VTime = VTime(this.toInstant())
fun LocalDateTime.toQuadTime(offset: ZoneOffset): VTime = VTime(this.toInstant(offset))
fun LocalDate.toQuadTime(offset: ZoneOffset): VTime = atStartOfDay().toQuadTime(offset)
fun LocalTime.toQuadTime(offset: ZoneOffset): VTime = atDate(LocalDate.MIN).toQuadTime(offset)
