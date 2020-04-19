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
 *   @File: AmountUnit.kt
 *   @Author:  lcz20@163.com
 *   @LastModified:  2020-04-19 21:50:34
 */

package cn.zenliu.kotlin.util.measure.unit

/**
 * 物质的量单位
 * @property text String
 * @property symbol String
 * @property metric MetricPrefixes
 * @property aliasOfUnit Array<AmountUnit>?
 * @constructor
 */
enum class AmountUnit(
	override val text: String,
	override val symbol: String,
	override val metric: MetricPrefixes = MetricPrefixes.None,
	override val aliasOfUnit: Array<AmountUnit>? = null
) : CommonUnits<AmountUnit> {
	mole("摩尔", "mol"),
	mol("摩", "mol", aliasOfUnit = arrayOf(mole)),
	;

	companion object {
		val amountOfMole = PhysicsContant.Na
	}
}

