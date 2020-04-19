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
 *   @File: Constant.kt
 *   @Author:  lcz20@163.com
 *   @LastModified:  2020-04-19 21:50:34
 */

package cn.zenliu.kotlin.util.measure.unit


object PhysicsContant {
	/**
	 * Planck constant
	 */
	val h = "6.626070040E-34".toBigDecimal()

	/**
	 * Avogadro constant 2018-11-16 SI
	 */
	val Na = "6.02214076E23".toBigDecimal()
	val L = Na

	/**
	 * light speed
	 */
	val c = "299792458".toBigDecimal()

	/**
	 * Elementary charge 2019 SI
	 */
	val e = "1.602176634E−19".toBigDecimal()
}
