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
 *   @File: VTime.kt
 *   @Author:  lcz20@163.com
 *   @LastModified:  2020-04-11 16:31:13
 */

package cn.zenliu.kotlin.util.nquad.quad.value

import cn.zenliu.kotlin.util.nquad.quad.*
import cn.zenliu.kotlin.util.nquad.quad.schema.*
import java.time.*

class VTime(override val native: Instant) : Value<Instant> {
	val typed: VTyped<Instant> = VTyped(this, Global.defaultTimeType)
	override fun toString(): String = native.toEpochMilli().toString()
}

