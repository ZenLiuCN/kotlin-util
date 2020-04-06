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
 *   @Module: case-style
 *   @File: Extension.kt
 *   @Author:  lcz20@163.com
 *   @LastModified:  2020-04-05 16:08:23
 */

package cn.zenliu.kotlin.util.casestyle


fun String.guessStyle(unicode: Boolean = false): CaseStyle? = if (unicode)
	UnicodeStyle.values().find { it.validate(this) }
else
	AsciiStyle.values().find { it.validate(this) }


fun String.formatStyle(from: UnicodeStyle, to: UnicodeStyle) = to.format(this, from)
fun String.formatStyle(from: AsciiStyle, to: AsciiStyle) = to.format(this, from)

fun String.toStyle(style: AsciiStyle) = guessStyle(false)?.let { if (it == style) this else style.format(this, it) }
fun String.toStyle(style: UnicodeStyle) = guessStyle(true)?.let { if (it == style) this else style.format(this, it) }


fun String.toCamel(unicode: Boolean = false) = if (unicode) toStyle(UnicodeStyle.Camel) else toStyle(AsciiStyle.Camel)
fun String.toPascal(unicode: Boolean = false) = if (unicode) toStyle(UnicodeStyle.Pascal) else toStyle(AsciiStyle.Pascal)
fun String.toSnake(unicode: Boolean = false) = if (unicode) toStyle(UnicodeStyle.Snake) else toStyle(AsciiStyle.Snake)
fun String.toSnakeUpper(unicode: Boolean = false) = if (unicode) toStyle(UnicodeStyle.SnakeUpper) else toStyle(AsciiStyle.SnakeUpper)
fun String.toKebab(unicode: Boolean = false) = if (unicode) toStyle(UnicodeStyle.Kebab) else toStyle(AsciiStyle.Kebab)
fun String.toKebabUpper(unicode: Boolean = false) = if (unicode) toStyle(UnicodeStyle.KebabUpper) else toStyle(AsciiStyle.KebabUpper)


