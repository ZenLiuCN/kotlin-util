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
 *   @File: MetricPrefixes.kt
 *   @Author:  lcz20@163.com
 *   @LastModified:  2020-04-19 22:10:23
 */

package cn.zenliu.kotlin.util.measure.unit

import java.math.BigDecimal

/**
 *
 * @property scale Int
 * @property text String
 * @property symbol String
 * @property aliasOfUnit List<MetricPrefixes>?
 * @constructor
 */

enum class MetricPrefixes(
	val scale: Int = 0,
	override val text: String,
	override val symbol: String,
	override val aliasOfUnit: Array<MetricPrefixes>? = null
) : MetricPrefix<MetricPrefixes> {
	/**
	 * 尧（尧它）	佑	yotta	Y	10008	1024	1000000000000000000000000	1991
	 */
	yotta(24, "尧", "Y"),

	/**
	 * 泽（泽它）	皆	zetta	Z	10007	1021	1000000000000000000000	1991
	 *
	 */
	zetta(21, "泽", "Z"),

	/**
	 *艾（艾可萨）	艾	exa	E	10006	1018	1000000000000000000	1975
	 */
	exa(18, "艾", "E"),

	/**
	 *拍（拍它）	拍	peta	P	10005	1015	1000000000000000	1975
	 */
	peta(15, "拍", "P"),
	P(15, "拍", "P", arrayOf(peta)),

	/**
	 *	太（太拉）	兆	tera	T	10004	1012	1000000000000	1960
	 */
	tera(12, "太", "T"),
	T(12, "太", "T", arrayOf(tera)),

	/**
	 *	吉（吉咖）	吉	giga	G	10003	109	1000000000	1960
	 */
	giga(9, "吉", "G"),
	G(9, "吉", "G", arrayOf(giga)),

	/**
	 *	兆	百萬	mega	M	10002	106	1000000	1873
	 */
	mega(6, "兆", "M"),
	M(6, "兆", "M", arrayOf(mega)),

	/**
	 *	千	千	kilo	k	10001	103	1000	1795
	 */
	kilo(3, "千", "k"),
	k(3, "千", "k", arrayOf(kilo)),

	/**
	 *	百	百	hecto	h	10002/3	102	100	1795
	 */
	hecto(2, "百", "h"),

	/**
	 *	十	十	deca	da	10001/3	101	10	1795
	 */
	deca(1, "十", "da"),

	None(0, "", ""),

	/**
	 *	分	分	deci	d	1000−1/3	10-1	0.1	1795
	 */
	deci(-1, "分", "d"),
	d(-1, "分", "d", arrayOf(deci)),

	/**
	 *厘	厘	centi	c	1000−2/3	10-2	0.01	1795
	 */
	centi(-2, "厘", "c"),
	c(-2, "厘", "c", arrayOf(centi)),

	/**
	 *毫	毫	milli	m	1000-1	10-3	0.001	1795
	 */
	milli(-3, "毫", "m"),
	m(-3, "毫", "m", arrayOf(milli)),

	/**
	 *微	微	micro	µ	1000-2	10-6	0.000001	1873
	 */
	micro(-6, "微", "µ"),
	µ(-6, "微", "µ", arrayOf(micro)),

	/**
	 *纳（纳诺）	奈	nano	n	1000-3	10-9	0.000000001	1960
	 */
	nano(-9, "纳", "n"),
	n(-9, "纳", "n", arrayOf(nano)),

	/**
	 *皮（皮可）	皮	pico	p	1000-4	10-12	0.000000000001	1960
	 */
	pico(-12, "皮", "p"),
	p(-12, "皮", "p", arrayOf(pico)),

	/**
	 *飞（飞母托）	飛	femto	f	1000-5	10-15	0.000000000000001	1964
	 */
	femto(-15, "飞", "f"),

	/**
	 *阿（阿托）	阿	atto	a	1000-6	10-18	0.000000000000000001	1964
	 */
	atto(-18, "阿", "a"),

	/**
	 *仄（仄普托）	介	zepto	z	1000-7	10-21	0.000000000000000000001	1991
	 */
	zepto(-21, "仄", "z"),

	/**
	 *幺（幺科托）	攸	yocto	y	1000-8	10-24	0.000000000000000000000001	1991
	 */
	yocto(-24, "幺", "y"),
	;

	/**
	 * get scale to a target MetricPrefixes
	 * if q is not MetricPrefixes return zero
	 */
	override fun scaleTo(q: MetricPrefixes): Quantity = when (q) {
		None, this -> BigDecimal.ONE
		else -> scaleBetween(this, q)
	}

	companion object {
		private fun scaleBetween(a: MetricPrefixes, b: MetricPrefixes): Quantity =
			when (a) {
				b -> BigDecimal.ONE
				else -> BigDecimal.TEN.pow(a.scale - b.scale)
			}
	}
}
