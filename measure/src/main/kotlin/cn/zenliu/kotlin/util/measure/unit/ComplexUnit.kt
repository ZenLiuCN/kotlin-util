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
 *   @File: ComplexUnit.kt
 *   @Author:  lcz20@163.com
 *   @LastModified:  2020-04-19 21:50:34
 */

package cn.zenliu.kotlin.util.measure.unit

/**
 * 复合单位
 * @property sequence Array<CommonUnits<*>>
 * @property name String
 * @property text String
 * @property symbol String
 * @property aliasOfUnit Array<ComplexUnit>?
 * @constructor
 */
open class ComplexUnit(
	val sequence: MutableList<CommonUnits<*>>
) : Units<ComplexUnit> {
	override val name: String
		get() = sequence.joinToString("") { it.name }
	override val text: String
		get() = sequence.joinToString("") { it.text }
	override val symbol: String
		get() = sequence.joinToString("") { it.symbol }
	override val aliasOfUnit: Array<ComplexUnit>? = null

	operator fun plus(u: CommonUnits<*>) {
		if (sequence.size > 1) {
			sequence.add(0, UnitOperator.be)
			sequence.add(UnitOperator.ed)
		}
		sequence.add(UnitOperator.plus)
		sequence.add(u)
	}

	operator fun times(u: CommonUnits<*>) {
		if (sequence.size > 1) {
			sequence.add(0, UnitOperator.be)
			sequence.add(UnitOperator.ed)
		}
		sequence.add(UnitOperator.times)
		sequence.add(u)
	}

	operator fun div(u: CommonUnits<*>) {
		if (sequence.size > 1) {
			sequence.add(0, UnitOperator.be)
			sequence.add(UnitOperator.ed)
		}
		sequence.add(UnitOperator.div)
		sequence.add(u)
	}

	fun square() {
		if (sequence.size > 1) {
			sequence.add(0, UnitOperator.be)
			sequence.add(UnitOperator.ed)
		}
		sequence.add(UnitOperator.square)


	}

	fun cube() {
		if (sequence.size > 1) {
			sequence.add(0, UnitOperator.be)
			sequence.add(UnitOperator.ed)
		}
		sequence.add(UnitOperator.cube)
	}

	companion object {
		fun squareOf(u: CommonUnits<*>) = ComplexUnit(mutableListOf(
			u, UnitOperator.square
		))

		fun cubeOf(u: CommonUnits<*>) = ComplexUnit(mutableListOf(
			u, UnitOperator.cube
		))
	}
}
