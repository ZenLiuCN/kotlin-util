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
 *   @Module: measure
 *   @File: DrugSpecification.kt
 *   @Author:  lcz20@163.com
 *   @LastModified:  2020-04-19 22:10:14
 */

package cn.zenliu.kotlin.util.measure


import java.math.BigDecimal

/**
 *
 *
 */
class DrugSpecification(
	raw: String? = null,
	units: List<Pair<BigDecimal, String>>? = null
) {
	val string: String
	val formula: List<Pair<BigDecimal, String>>

	init {
		when {
			raw != null -> {
				string = raw
				when {
					string.contains(":") -> {
						formula = string.split(":").map {
							it.fold(StringBuilder() to StringBuilder()) { acc, c ->
								when {
									c.isDigit() -> acc.first.append(c)
									else -> acc.second.append(c)
								}
								acc
							}.let { (count, unit) ->
								(count.toString().takeIf { it.isNotBlank() }?.toBigDecimal()
									?: 1.toBigDecimal()) to unit.toString()
							}
						}
					}
					string.contains("/") -> {
						formula = string.split("/").map {
							it.fold(StringBuilder() to StringBuilder()) { acc, c ->
								when {
									c.isDigit() -> acc.first.append(c)
									else -> acc.second.append(c)
								}
								acc
							}.let { (count, unit) ->
								(count.toString().takeIf { it.isNotBlank() }?.toBigDecimal()
									?: 1.toBigDecimal()) to unit.toString()
							}
						}
					}
					else -> throw IllegalArgumentException("absent of raw string or formula of specification")

				}
			}
			units != null -> {
				formula = units
				string = formula.joinToString("/") { it.first.toPlainString() + "/" + it.second }

			}
			else -> throw IllegalArgumentException("absent of raw string or formula of specification")
		}

	}

	companion object {
		fun ofString(raw: String) = DrugSpecification(raw)
	}
}