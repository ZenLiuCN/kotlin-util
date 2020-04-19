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
 *   @File: Units.kt
 *   @Author:  lcz20@163.com
 *   @LastModified:  2020-04-19 21:50:35
 */

package cn.zenliu.kotlin.util.measure.unit

/**
 * 单位
 * @property name String 名称
 * @property text String 文本
 * @property symbol String 符号
 * @property aliasOfUnit List<Unit>? 别名
 */
interface Units<T : Units<T>> : Measure {
	val name: String
	val text: String
	val symbol: String
	val aliasOfUnit: Array<T>?
}

