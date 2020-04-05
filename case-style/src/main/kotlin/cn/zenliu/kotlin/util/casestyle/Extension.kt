/*
 *     Copyright (c) 2020.  Zen.Liu All Rights Reserved.
 *     @Project: kotlin-utils
 *     @Module: case-style
 *     @File: Extension.kt
 *     @Author:  lcz20@163.com
 *     @LastModified:  2020-04-05 15:03:44
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


