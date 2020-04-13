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
 *   @File: LongBitArrayTest.kt
 *   @Author:  lcz20@163.com
 *   @LastModified:  2020-04-13 23:01:17
 */

package cn.zenliu.kotlin.util.compress.bitmap

import org.junit.jupiter.api.*
import org.junit.jupiter.api.Assertions.*

internal class LongBitArrayTest {
	@Test
	fun maskGenerateInt() {
		(31 downTo 0).forEach {
			println((1 shl it).let {
				"0x" + it.toString(16).padStart(8, '0')
			})
		}
	}


	@Test
	fun intBitArrayTest() {
		val ba = IntBitArray(6552222)
		val i = (1..6552222)
		val checker = mutableListOf<Int>(0)
		(0..1000).forEach { _ ->
			val a = i.random()
			print(a)
			print(',')
			checker.add(a)
			ba.setBit(a)
		}
		ba.setBit(0)
		checker.forEach {
			assertEquals(ba.getBit(it), true, "$it should be true")
		}
		(0..1000).forEach {
			if (!checker.contains(it)) {
				assertEquals(ba.getBit(it), false, "$it should not be true")
			}
		}
	}
}
