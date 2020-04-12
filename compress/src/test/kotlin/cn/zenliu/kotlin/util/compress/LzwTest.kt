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
 *   @Module: compress
 *   @File: LzwTest.kt
 *   @Author:  lcz20@163.com
 *   @LastModified:  2020-04-11 21:36:45
 */

package cn.zenliu.kotlin.util.compress

import org.junit.jupiter.api.*
import kotlin.test.*

internal class LzwTest {
	@Test
	fun lzw() {
		val source = "TOBEORNOTTOBEORTOBEORNOT abc 12304"
		val com = LZW.compressASCII(source).apply { println(this) }
		assertEquals(source, LZW.decompressASCII(com).apply { println(this) })
		com.joinToString("") { it.toByte().toChar().toString() }.apply { println(this) }
	}
}
