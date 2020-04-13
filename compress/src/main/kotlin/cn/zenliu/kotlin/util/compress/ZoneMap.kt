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
 *   @File: ZoneMap.kt
 *   @Author:  lcz20@163.com
 *   @LastModified:  2020-04-13 01:43:00
 */

package cn.zenliu.kotlin.util.compress

import java.util.*

object ZoneMap {
	private val masks = longArrayOf(
		-0x800000000000000,
		0x400000000000000,
		0x200000000000000,
		0x100000000000000,
		0x080000000000000,
		0x040000000000000,
		0x020000000000000,
		0x010000000000000,
		0x008000000000000,
		0x004000000000000,
		0x002000000000000,
		0x001000000000000,
		0x000800000000000,
		0x000400000000000,
		0x000200000000000,
		0x000100000000000,
		0x000080000000000,
		0x000040000000000,
		0x000020000000000,
		0x000010000000000,
		0x000008000000000,
		0x000004000000000,
		0x000002000000000,
		0x000001000000000,
		0x000000800000000,
		0x000000400000000,
		0x000000200000000,
		0x000000100000000,
		0x000000080000000,
		0x000000040000000,
		0x000000020000000,
		0x000000010000000
		, 0x000000008000000
		, 0x000000004000000
		, 0x000000002000000
		, 0x000000001000000
		, 0x000000000800000
		, 0x000000000400000
		, 0x000000000200000
		, 0x000000000100000
		, 0x000000000080000
		, 0x000000000040000
		, 0x000000000020000
		, 0x000000000010000
		, 0x000000000008000
		, 0x000000000004000
		, 0x000000000002000
		, 0x000000000001000
		, 0x000000000000800
		, 0x000000000000400
		, 0x000000000000200
		, 0x000000000000100
		, 0x000000000000080
		, 0x000000000000040
		, 0x000000000000020
		, 0x000000000000010
		, 0x000000000000008
		, 0x000000000000004
		, 0x000000000000002
		, 0x000000000000001


	)
	private val intMasks = intArrayOf(
		-0x80000000, 0x40000000, 0x20000000, 0x10000000,
		0x08000000, 0x04000000, 0x02000000, 0x01000000,
		0x00800000, 0x00400000, 0x00200000, 0x00100000,
		0x00080000, 0x00040000, 0x00020000, 0x00010000,
		0x00008000, 0x00004000, 0x00002000, 0x00001000,
		0x00000800, 0x00000400, 0x00000200, 0x00000100,
		0x00000080, 0x00000040, 0x00000020, 0x00000010,
		0x00000008, 0x00000004, 0x00000002, 0x00000001)
	private var dict: LongArray = longArrayOf()
	private var bitSize: Int = 0
	private var intSize: Int = 0
	private var first = Int.MAX_VALUE // The index where first set bit is
	private var last = Int.MIN_VALUE // The _INTEGER INDEX_ where last set bit is
	private var min: Int = 0
	private var max: Int = 0
	private fun LongArray.check(v: Int): Boolean {
		return if (v !in min..max) false else get(v ushr 6) and masks[v % 64] != 0L
	}

	private fun LongArray.put(bit: Int) {
		if (bit >= bitSize) return
		val i: Int = bit ushr 6
		if (i < first) first = i
		if (i > last) last = i
		this[i] = get(i) or masks[bit % 64]
	}

	private fun LongArray.next(startBit: Int): Int {
		var sb = startBit
		((sb ushr 5)..intSize).forEach { i ->
			val bits: Long = get(i)
			if (bits != 0L) {
				for (b in sb % 32..31) {
					if (bits and masks[b] != 0L) {
						return (i shl 5) + b
					}
				}
			}
			sb = 0
		}
		return -1
	}

	internal fun load(data: LongArray, size: Int) {
		dict = data
		bitSize = if (size < 32) 32 else size
		bitSize = (intSize ushr 5) + 1
	}

	private fun Int.proc() = this - min
	internal fun init(min: Int, max: Int) {
		val size = max - min + 1
		assert(size > 1) { "$max is not bigger than $min" }
		this.min = min
		this.max = max
		bitSize = if (size < 64) 64 else size
		intSize = (bitSize ushr 6) + 1
		dict = LongArray(intSize + 1)
	}

	internal fun data(): LongArray = dict.clone()
	internal fun String.compress() = run {
		val dict = this.toSet().toList().sorted().toCharArray()
		var cnt = 0
		buildString {
			append(dict.size.toString().padStart(6, '0'))
			append(dict)
			this@compress.forEachIndexed { i, c ->
				val idx = dict.indexOf(c).toString()[0]
				when {
					i == 0 -> append(idx)
					last() == idx -> cnt++
					cnt != 0 -> {
						append('.')
						append(cnt)
						append('.')
						cnt = 0
					}
					else -> append(idx)
				}
			}
		}

	}

	internal fun dump(): String = run {
		dict.clone()
			.map { it.toBigInteger().toByteArray().toList() }
			.flatten().toByteArray()
			.let { Base64.getEncoder().encodeToString(it) }
			.compress()
	}

	internal fun put(code: Int) {
		dict.put(code.proc())
	}

	fun check(code: Int): Boolean = dict.check(code.proc())

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

