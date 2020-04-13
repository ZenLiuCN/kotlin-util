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
 *   @File: ChinaHelper.kt
 *   @Author:  lcz20@163.com
 *   @LastModified:  2020-04-14 00:11:37
 */

package cn.zenliu.kotlin.util.compress

import cn.zenliu.kotlin.util.compress.bitmap.*
import java.time.*


object ChinaHelper {

	private const val source = "|m|<311|8000|<2|e7d|-1000000|<308|m|<2|fe3ff00|<308|8000|<2|adf|57fc00|<1|58000bd|-3fff2c00|<1|58000b|-5fff9800|<2|-447ffe0d|-10000000|4000|b00007f|-1000000|600|a3c001|-40020000|78|b6c00|3fb80000|<1|b800|6f80000|<1|40000b00|7fe000|<1|78000b0|3e800|<1|60000b|7f80|<1|20000|<275|m|<2|87e0070|<1|400|800f07|-20000000|<1|b0100|60000000|<1|9e00|17f0000|<1|a00|6c0000|<1|40000b0|70000|<1|40000a|7fc0|<1|40000|-5ffff802|<1|6000|a00007f|-8000000|400|a00007|-200000|60|a0000|7fe00000|6|<275|8000|<2|bc0|7c0000|<1|bf|70000|<1|b|m|<2|-47fff812|<2|a00007e|<1|400|b00007|-10000000|<1|b0000|7f000000|7|-3fff6000|7e00000|<1|a00|7fc000|<1|4000000|<37|e00007|m|<7|e00|3ff000|<11|8000070|<1783|8000|<2|be1|-fe80000|<1|40000b8|1e008000|<1|50000b|-7effb000|<1|40000|-47eff900|<2|bc00060|<2|b80000|m|60|b0100|3000000|6|b810|<2|60000bc1|600000|<1|bc|10040000|<1|40000b|-7fffe000|<2|-57fffa80|<1|6000|b000068|<1|600|b80006|<1|40|<266|m|<2|be0c020|<1|300|b81004|<1|78|b0000|30000000|6|b000|6000000|<1|b00|580000|<1|60000a4|70000|<1|40000a|7000|<1|40000|-5ffffa00|<1|6000|<47|-1ffff60|<236|8000|<2|b8f|-3fe04000|<1|18000bf|-7ffa2900|<1|40000b|-fffc000|<1|60000|-40fffa00|<2|b600078|<2|be0007|m|<1|80007|3e000001|4|9c10|2280000|<1|70000b80|400000|<1|bc|4000|<1|5e000a|5800|<1|60000|-5ffff820|<1|7000|<43|c|26000|<2414|8000|<2|cf6|-1800000|1000000|<307|m|<2|ae17e00|<2|861600|<1|60|b4180|68000000|6|a81c|<2|4000087c|<1|10|74000a0|18010000|<1|6c0009|30003800|<2|-67f3ffdd|<2|b80007c|<1|400|b00801|<1|48|a0180|<1|7|b800|<2|70000a01|380000|<270|8000|<2|bef|-7fdf0000|<1|2000096|1c006000|<1|60000b|-3ffff440|<1|70000|-5feffb80|<1|7000|b000070|<2|b80000|m|50|b0000|13000000|7|-7fff5000|2c00000|<1|40000b00|600000|<1|b8|38000|<1|70000a|7f00|<1|40000|<275|m|<2|b810078|<1|400|b18007|40000000|<1|b8100|70000000|<1|be00|6000000|<1|9a0|700000|<1|b8|40000|<1|8|61002000|<2|-4feffd88|<1|6000|<3|b80007|m|<1|b0000|2e000000|6|b800|6600000|<1|20000a00|780000|<4|b|-7fffc400|<2|-5ffff900|<2|a000070|<2|a00007|-40000000|40|<253|8000|<2|bc1|-7f838000|<1|4000096|1c000000|<1|b|-3fffe000|<2|-4ffffa02|<1|4000|bc0004f|<1|700|b00003|-4000000|40|b0000|7c000000|7|b000|5c00000|<1|40000a00|7e0000|<1|6000000|<281|m|<2|bc18058|<2|b00002|<1|40|a0040|70000000|<1|b800|1ee0000|<1|70000a00|400000|<1|b0|<2|40000b|-7fffc001|<1|40000|-4ffff802|<1|4000|a00007e|<1|700|b00007|-6000000|<1|b0000|5fe00000|4|<275|8000|<2|bc0|-3f60000|<1|b0|1f000000|<1|54000b|-1fff9000|<2|-42000000|<1|4000|b400030|<2|a01c00|200000|7f|bc000|c000000|7|-1fff7fe8|3f8000|<1|50000a01|500000|<1|30000b0|<2|30000b|6000|<5|a01807f|-40000000|<1|b00003|-8000000|60|a0000|7e000000|4|b000|7400000|<1|40000b00|7e8000|<1195|8000|<2|be8|200000|<1|7c000bc|8074000|<1|b|-1effc040|<1|40000|-47eff8c0|<1|6000|b600033|<1|400|b01006|<2|b8100|4f800000|6|b810|7400000|<1|30000a00|338000|<1|b0|c000|<1|60000b|-7fffa000|<2|-4ffffb80|<1|6000|b00007f|-20000000|400|b00007|-20000000|40|b0000|7f800000|<1|a000|7f80000|<1|40000a00|7fc000|<226|40|<31|m|<2|bf1fc00|<2|bc0002|<1|40|b8000|3c000000|4|<3|be0|7c000|<1|70000a3|e000|<1|38000b|m|<2|-57fffe00|<1|6000|a000070|<1|680|b00003|m|51|a0000|7f000000|6|a000|7800000|<1|40000900|400000|<1|4000000|<43|e000|3780000|<192|e0004|<30|8000|<2|bc1|-7fc00000|<1|60000b8|1801c000|<1|40000a|-7fffc000|<1|60000|-7877f860|<1|6000|b01007d|-40000000|400|b01005|-60000000|60|b0000|7e000000|4|a010|6000000|<1|b00|700000|<1|40000b0|7f800|<1|40000b|7fc0|<2|-5ffff802|<1|4000|a000060|<1|600|<53|c0000|3f200000|<214|m|<2|9e1f600|<2|9c0002|-7b400000|60|9ff00|<2|b800|<2|811|-ff00000|<1|8f|m|<1|9|-40000000|<1|5c000|-47effec0|<1|7000|a800000|<1|700|<6|b800|1e00000|<1|8000b00|380000|<1|b0|3b000|<1|40000a|5000|<1|40000|-5ffff840|<2|a800040|<1|400|b00005|60000000|60|80000|<2|8000|<2|800|<93|b0000|20000000|<1|b000|2800000|<1|40000b00|600000|<1|4000000|<143|8000|<2|b5e|1f0000|<1|be|3e000|<1|b|-3e7fa018|<1|40000|-69fff900|<1|4000|b008040|<2|b00004|<1|40|b0000|60000000|<1|b800|4000000|<1|40000b00|780000|<1|40000a0|7bf00|<1|40000b|7000|<2|-4ffff804|<2|a000078|<1|400|a00007|-40000000|40|<266|m|<2|8780000|<2|bc0000|<2|80000|70000000|<1|8000|<268|670007|-2000000|<967|m|<2|fffff80|1f8|<1|800000|75ef000|<304|8000|<2|8f8|-3bfa800|<1|7c00000|<2|b|-7effa000|<2|-4feffa00|<2|b80006c|<2|980001|<1|70|9c000|37000000|4|a018|7800000|<1|980|700000|<1|a0|1000c000|<1|10000a|1c01a4c|<1|40000|<3|b80007c|<1|400|b00005|-40000000|<1|b8000|1fc00000|<1|b000|7000000|<1|40000b00|3c0000|<1|40000b0|3f000|<1|b|7000|<2|-5ffffa00|<36|c000|7fbc000|<1|c00|3fffe0|<1|c0|3fffc|<205|m|<2|b01d070|<1|400|d00004|<1|40|b8000|3fe00000|6|b000|3c00000|<1|a00|7f0000|<1|b0|7f800|<51|e|1f80|<8|c00003|-80000|<1|e0000|37f80000|<226|8000|<2|b01|-ff04000|<1|4000000|<2|b|-7fffc200|<1|40000|-4ffffc08|<2|a000058|<1|400|a00007|-4000000|40|a0000|78000000|<1|a000|7fc0000|<1|a00|7f0000|<42|c0000|3fd00000|<4|f80|1df800|<1|c0|3f800|<5|-3ffffd00|<2|c00003f|-8000000|<4|b0000|38000000|<4|c00|1c0000|<1|c0|30000|<205|m|<2|b800079|<2|a00007|-40000|<1|a0000|7fe00000|<1|a000|7e00000|<1|a00|7ff000|<1|a0|7fe00|<58|800007f|<2108|8000|<2|b81|-1d80000|<1|b8|20000|<1|b|-7fffc410|<2|-47fffc11|<1|6000|b00003f|m|600|b00007|7f800000|<1|b0000|3fe00000|<1|b000|2ff0000|<1|40000a00|7fc000|<1|a0|7e000|<280|m|<2|bc10070|<2|800000|<2|a0000|40000000|<1|b000|7000000|<1|b00|7c0000|<1|a0|70000|<1|a|7c00|<2|-5ffff8a0|<1|4000|a000078|<1|600|a00007|-10000000|<1|a0000|7e000000|<1|a000|7f80000|<51|c00|7f0000|<1|c0|7f000|<217|8000|<2|bc0|700000|<1|b0|3c000|<61|800007|m|<1|80000|78000000|<4|800|7c0000|<1|80|7e000|<1|c|3e00|<2|-ffff8c0|<224|m|<2|8e00060|<1|400|a40004|<2|b0000|18000000|4|a000|3c00000|<1|a00|600000|<295|8000|<2|bf4|400000|<1|bc|<6|-5ffffa00|<2|a000060|<55|e0000|1d800000|<10|e|3000|<2|-3ffffc04|<2|c00003f|-40000000|<1|c00003|m|<1|c0000|7ff00000|<1|c000|7f00000|<23|b800|7f80000|<4|e0|5e000|<1|c|7e00|<145|7f|-40000000|<1592|8000|<3124|ffff|-20000000|<311|-800000|>186a0,c8328,0,57e4,afc89,57e5"
	private lateinit var bits: IntBitArray
	val initialized get() = this::bits.isInitialized
	private var min = Int.MIN_VALUE
	private var max = Int.MAX_VALUE
	private var lock = false
	private val Int.dump
		get() = when (this) {
			Int.MIN_VALUE -> "|m|"
			Int.MAX_VALUE -> "|M|"
			else -> "|${toString(16)}|"
		}

	fun dump(): String {
		var cnt = 0
		return bits.data().joinToString("") { n ->
			when {
				n == 0 -> {
					cnt++
					""
				}
				cnt != 0 -> {
					val a = "|<$cnt|${n.dump}"
					cnt = 0
					a
				}
				else -> n.dump
			}
		}.replace("||", "|") + ">${min.toString(16)},${max.toString(16)},${bits.bitFirst.toString(16)},${bits.bitLast.toString(16)},${bits.bitSize.toString(16)},${bits.intSize.toString(16)}"
	}

	fun load(data: String?) {
		val src = data ?: source
		val (ar, info) = src.split(">").takeIf { it.size == 2 }
			?: throw IllegalArgumentException("data format error")
		val p = info.split(",").takeIf { it.size == 6 }?.map { it.toInt(16) }
			?: throw IllegalArgumentException("data format error")
		val (mi, mx, bf, bl, bs) = p
		val ds = p[5]
		val arr = ar.split(",").map {
			mutableListOf<Int>().apply {
				when {
					it.contains("|") ->
						it.split("|").forEach {
							when {
								it.isBlank() -> Unit
								it == "m" -> add(Int.MIN_VALUE)
								it == "M" -> add(Int.MAX_VALUE)
								it.startsWith("<") -> it.replace("<", "").toInt().apply {
									(0 until this).forEach { _ ->
										add(0)

									}
								}
								else -> add(it.toInt(16))
							}
						}

					else -> add(it.toInt(16))
				}
			}
		}.flatten().takeIf { it.size == ds }?.toIntArray()
			?: throw IllegalArgumentException("data size not match $ds error")
		min = mi
		max = mx
		bits = IntBitArray(bs, arr)
		bits.bitFirst = bf
		bits.bitLast = bl
		lock = true
	}

	fun put(b: Int) {
		if (lock) throw IllegalStateException("Zone Map is locked by load a data ")
		bits.setBit(b - min)
	}

	fun check(b: Int): Boolean = if (b !in min..max) false else bits.getBit(b - min)
	fun init(min: Int, max: Int) {
		if (lock) throw IllegalStateException("Zone Map is locked by load a data ")
		this.max = max
		this.min = min
		bits = IntBitArray(max - min + 1)
	}

	private val idParam = arrayOf(7, 9, 10, 5, 8, 4, 2, 1, 6, 3, 7, 9, 10, 5, 8, 4, 2, 1)
	fun validateCitizenId(id: String, birthYear: IntRange = 1900..LocalDate.now().year): Boolean {
		if (!initialized) this.load(null)
		if (id.length != 18) return false
		if (id.substring(0, 5).toIntOrNull()?.let(::check) != true) return false
		val year = id.substring(6, 9).toIntOrNull()?.takeIf { it > 1900 && it in birthYear } ?: return false
		val month = id.substring(10, 11).toIntOrNull()?.takeIf { it in 1..12 } ?: return false
		val dayOfMonth = id.substring(12, 13).toIntOrNull()?.takeIf { it in 1..31 } ?: return false
		kotlin.runCatching {
			LocalDate.of(year, month, dayOfMonth)
		}.onFailure {
			return false
		}
		val chkSum = id.mapIndexed { idx, c -> (if (c.toLowerCase() == 'x') 10 else c.toString().toInt()) * idParam[idx] }.sum() % 11
		return chkSum == 1
	}

	/**
	 * 是否地级市
	 * @param code Int
	 * @return Boolean
	 */
	fun isPrefecturalCity(code: Int) = (code % 100) > 80

	/**
	 * 是否县级市|旗
	 * @param code Int
	 * @return Boolean
	 */
	fun isCountyCity(code: Int) = (code % 100) in 21..80

	/**
	 * 是否市辖区|地区县
	 * @param code Int
	 * @return Boolean
	 */
	fun isCityZone(code: Int) = (code % 100) < 21

	/**
	 * 是否自治区/盟
	 * @param code Int
	 * @return Boolean
	 */
	fun isPrefecture(code: Int) = (((code - (code % 100)) % 10000) / 100) in 21..50

	/**
	 * 是否省辖市
	 * @param code Int
	 * @return Boolean
	 */
	fun isProvincialMunicipalityCity(code: Int) = (((code - (code % 100)) % 10000) / 100) !in 21..50
}

