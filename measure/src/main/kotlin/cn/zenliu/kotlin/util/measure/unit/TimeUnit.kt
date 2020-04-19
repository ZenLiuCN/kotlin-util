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
 *   @File: TimeUnit.kt
 *   @Author:  lcz20@163.com
 *   @LastModified:  2020-04-19 21:50:34
 */

package cn.zenliu.kotlin.util.measure.unit

/**
 * 时间单位
 * @property text String
 * @property symbol String
 * @property metric MetricPrefixes
 * @property aliasOfUnit Array<TimeUnit>?
 * @property scale int how many of base unit
 * @constructor
 */
enum class TimeUnit(
	override val text: String,
	override val symbol: String,
	override val metric: MetricPrefixes = MetricPrefixes.None,
	override val aliasOfUnit: Array<TimeUnit>? = null,
	val scale: Int = 1
) : CommonUnits<TimeUnit> {
	second("秒", "s"),
	s("秒", "s", aliasOfUnit = arrayOf(second)),
	ms("毫秒", "ms", MetricPrefixes.milli),
	μm("毫秒", "μs", MetricPrefixes.micro),
	ns("纳秒", "ns", MetricPrefixes.nano),
	h("小时", "h", scale = 86400),
	m("分钟", "m", scale = 60),
	day("天", "day", scale = 86400 * 24),
	;

	override fun scaleTo(q: TimeUnit): Quantity = when {
		scale == 1 -> super.scaleTo(q)
		else -> metric.scaleTo(q.metric) * (scale.toBigDecimal() / q.scale.toBigDecimal())
	}
}
