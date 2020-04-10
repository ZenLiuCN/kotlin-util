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
 *   @Module: format
 *   @File: BinaryFormatTest.kt
 *   @Author:  lcz20@163.com
 *   @LastModified:  2020-04-11 00:10:48
 */

package cn.zenliu.kotlin.util.format

import cn.zenliu.kotlin.util.format.BinaryFormat.fromHexTinyV2
import cn.zenliu.kotlin.util.format.BinaryFormat.toHexTinyV2
import org.junit.jupiter.api.*

internal class BinaryFormatTest {
	val h = "0123456789abcdef"

	@Test
	fun testV2() {
		val utf = "d41d8cd98f00b204e9800998ecf8427e".chunked(2).map {
			h.indexOf(it[0]).shl(4) + h.indexOf(it[1]).toByte().toInt()
		}.joinToString("") { it.toChar().toString() }.apply { println(this);println(this.length) }
		utf.fold(StringBuilder()) { b, c ->
			c.toInt().let {
				b.append(h[it.shr(4).and(0xf)])
				b.append(h[it.and(0xf)])
			}
			b
		}.toString().apply { println(this);println(this.length) }

		"d41d8cd98f00b204e9800998ecf8427e".toHexTinyV2.apply { println(this);println(this.length) }
		"Ô\u001D\u008CÙ\u008F ²\u0004é\u0080\t\u0098ìøB~".fromHexTinyV2.apply { println(this);println(this.length) }
	}

}
