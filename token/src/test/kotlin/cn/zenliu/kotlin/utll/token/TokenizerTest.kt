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
 *   @Module: token
 *   @File: TokenizerTest.kt
 *   @Author:  lcz20@163.com
 *   @LastModified:  2020-04-06 18:50:16
 */

package cn.zenliu.kotlin.utll.token

import cn.zenliu.kotlin.utll.token.Tokenizer.generator
import cn.zenliu.kotlin.utll.token.Tokenizer.parser
import cn.zenliu.kotlin.utll.token.Tokenizer.setFormula
import org.junit.jupiter.api.*
import kotlin.system.*
import kotlin.test.*

internal class TokenizerTest {
	val formula = listOf(Long::class, String::class, String::class)
	val raw = listOf(1024L, "some user", "some 中午")
	val raw1 = listOf(1024L, "中午", "中午中午中午中午")
	val formula2 = listOf(Long::class, Byte::class, Long::class)
	val raw2 = listOf(1024L, 0x12, 112315465465L)
	val raw3 = listOf(1024L, 125, 5565664644L)

	@Test
	fun generate() {

		setFormula(formula)
		val token = generator(raw)
		assertNotNull(token)
		println(token)

		val res = parser(token)
		assertNotNull(res)
		assertEquals(raw, res.subList(0, res.lastIndex))

		val token1 = generator(raw1)
		assertNotNull(token1)
		println(token1)


		val res1 = parser(token1)
		assertNotNull(res1)
		assertEquals(raw1, res1.subList(0, res1.lastIndex))


	}

	@Test
	fun generateBenchmark() {
		println("-------with String-------")
		setFormula(formula)
		val token = generator(raw)!!
		val token1 = generator(raw1)!!
		measureNanoTime {
			(0 until 10000).forEach {

				generator(raw)!!
				generator(raw1)!!

			}
		}.apply { println("${this / (10000 * 2.0 * 1000)} ms/op for generate") }
		measureNanoTime {
			(0 until 10000).forEach {

				parser(token)
				parser(token1)

			}
		}.apply { println("${this / (10000 * 2.0 * 1000)} ms/op for parse") }
		println("-------with out String-------")
		setFormula(formula2)
		val token2 = generator(raw2)!!
		val token3 = generator(raw3)!!
		measureNanoTime {
			(0 until 10000).forEach {

				generator(raw2)!!
				generator(raw3)!!

			}
		}.apply { println("${this / (10000 * 2.0 * 1000)} ms/op for generate") }
		measureNanoTime {
			(0 until 10000).forEach {
				parser(token2)
				parser(token3)
			}
		}.apply { println("${this / (10000 * 2.0 * 1000)} ms/op for parse") }
	}
}
