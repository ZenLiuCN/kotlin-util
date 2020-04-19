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
 *   @File: VolumeUnit.kt
 *   @Author:  lcz20@163.com
 *   @LastModified:  2020-04-19 21:50:34
 */

package cn.zenliu.kotlin.util.measure.unit

/**
 * 体积单位
 * @property text String
 * @property symbol String
 * @property metric MetricPrefixes
 * @property aliasOfUnit Array<MetricPrefixes>?
 * @constructor
 */
enum class VolumeUnit(
	override val text: String,
	override val symbol: String,
	override val metric: MetricPrefixes = MetricPrefixes.None,
	override val aliasOfUnit: Array<VolumeUnit>? = null
) : CommonUnits<VolumeUnit> {
	Liter("升", "L"),
	L("升", "L", aliasOfUnit = arrayOf(Liter)),
	dL("分升", "dL", MetricPrefixes.deca),
	mL("毫升", "mL", MetricPrefixes.milli),
	μL("微升", "μL", MetricPrefixes.micro),
}

