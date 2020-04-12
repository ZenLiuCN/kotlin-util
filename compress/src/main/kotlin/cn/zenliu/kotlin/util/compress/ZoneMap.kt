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
	private lateinit var dict: BitSet
	internal fun load(data: ByteArray) {
		dict = BitSet.valueOf(data)
	}

	internal fun dump(): ByteArray = dict.toByteArray()

	internal fun put(code: Int) {
		dict[code] = true
	}

	fun check(code: Int) = dict[code]

	/**
	 * 是否地级市
	 * @param code Int
	 * @return Boolean
	 */
	fun isPrefecturalLevelCity(code: Int) = (code % 100) > 80

	/**
	 * 是否县级市|旗
	 * @param code Int
	 * @return Boolean
	 */
	fun isCountyLevelCity(code: Int) = (code % 100) in 21..80

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

