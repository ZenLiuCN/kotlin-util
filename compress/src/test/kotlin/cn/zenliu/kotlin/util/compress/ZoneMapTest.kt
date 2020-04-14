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
 *   @File: ZoneMapTest.kt
 *   @Author:  lcz20@163.com
 *   @LastModified:  2020-04-14 00:53:45
 */

package cn.zenliu.kotlin.util.compress


import cn.zenliu.kotlin.util.compress.TestData.codic
import cn.zenliu.kotlin.util.compress.TestData.raw
import cn.zenliu.kotlin.util.compress.TestData.rawData
import org.junit.jupiter.api.*
import kotlin.system.*
import kotlin.test.*

internal class ChinaHelperTest {




	fun validate(i: Int): Boolean {
		for (idx in raw) {
			if (idx == i) return true
		}

		return false
	}
	@Test
	fun idx(){
		val codes=rawData.keys.sorted().map{
			val l2=it % 100
			val f2=it/10000
			val m2=it/100-f2*100
			Triple(f2,m2,l2)
		}
		val (fs,ms,ls)=codes.fold(listOf(
			mutableSetOf<Int>(),
			mutableSetOf(),
			mutableSetOf()
		)){ acc,(f,m,l)->
			acc[0].add(f)
			acc[1].add(m)
			acc[2].add(l)
			acc
		}
		println("${fs.size} ,${ms.size},${ls.size}")
		val code=codes.map{(f,m,l)->
			buildString{
				append(codic[fs.indexOf(f)])
				append(codic[ms.indexOf(m)])
				append(codic[ls.indexOf(l)])
			}
		}
		println(code)
	}
	@Test
	fun generate(){
		val codes=rawData.keys.sorted()
		ChinaHelper.init(codes.min()!!, codes.max()!!)
		codes.forEach (ChinaHelper::put)
		print("total data size : ")
		codes.size.apply(::println)
		ChinaHelper.dump().apply {
			println("code dictionary length is :$length")
			println(this)

		}
		val charDict=rawData.map{(_,n)->
			n.toList()
		}.flatten().toSet().sorted().toList()
		println("total char dict size :${charDict.size} ")
		val nameDict=rawData.map{(c,n)->
			 c to n.toList().map{charDict.indexOf(it)}
		}.toMap()
		println(nameDict)
	}
	@Test
	fun justGenerate() {
		ChinaHelper.init(raw.min()!!, raw.max()!!)
		raw.forEach {
			ChinaHelper.put(it)
		}
		raw.size.apply(::println)
		ChinaHelper.dump().apply {
			println(this)
			println(this.length)

		}
	}

	@Test
	fun validate() {
		ChinaHelper.load(null)
		raw.filter { !ChinaHelper.check(it) }.apply { println(this) }
		(1000000 downTo 999999).filter { !raw.contains(it) }.filter { ChinaHelper.check(it) }.apply { println(this) }
		assertEquals(false, ChinaHelper.check(131088))
	}

	/**
	 * # memory 60 times larger with 100 times faster
	 *
	 *  |item            |bitmap              |indexOf              |forIn                |
	 *  |----------------|-------------------|---------------------|---------------------|
	 *  |memory of data  |180,008 ≈ 180Kib   |3,251 3KiB           |3,251 3KiB          |
	 *  |ns per op      | 5.0802313 ≈ 5      |499.79866975≈500     |490.5940168 ≈490≈500|
	 *
	 */
	@Test
	fun speed() {
		ChinaHelper.load(null)
		ChinaHelper.memoryBytes().apply { println("map memory bytes is $this") }
		raw.size.apply { println("memory bytes is $this") }
		(1..10000).map { _ ->
			measureNanoTime {
				(1..1000).forEach { _ ->
					assertEquals(true, ChinaHelper.check(820008))
					assertEquals(false, ChinaHelper.check(131088))
				}
			}
		}.sum().apply {
			println("bitmap ${this / 20000000.0} ns/op")
		}
		(1..10000).map { _ ->
			measureNanoTime {
				(1..1000).forEach { _ ->
					assertEquals(true, validate(820008))
					assertEquals(false, validate(131088))

				}
			}
		}.sum().apply {
			println("for in break ${this / 20000000.0} ns/op")
		}
		(1..10000).map {
			measureNanoTime {
				(1..1000).forEach { _ ->
					assertEquals(true, raw.indexOf(820008) > 0)
					assertEquals(false, raw.indexOf(131088) > 0)
				}
			}
		}.sum().apply {
			println("indexOf ${this / 20000000.0} ns/op")
		}


	}

	@Test
	fun zoneMapByLoad() {
		raw.size.apply(::println)
		ChinaHelper.load(null)

		measureNanoTime {
			raw.forEach {
				assertEquals(true, ChinaHelper.check(it), "$it miss hit")
			}
		}.apply {
			println("check ${(this / 100000) / 1000.0} ms/op")
		}


	}

	@Test
	fun testId() {
		ChinaHelper.validateCitizenId("510623198001011235").apply {
			assertEquals(false, this)
		}
	}
}
