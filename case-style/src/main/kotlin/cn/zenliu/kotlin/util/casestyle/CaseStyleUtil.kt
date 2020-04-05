/*
 *     Copyright (c) 2020.  Zen.Liu All Rights Reserved.
 *     @Project: kotlin-utils
 *     @Module: case-style
 *     @File: CaseStyleUtil.kt
 *     @Author:  lcz20@163.com
 *     @LastModified:  2020-04-05 15:39:32
 */

@file:JvmName("CaseStyleUtil")

package cn.zenliu.kotlin.util.casestyle


interface CaseStyle {
	val isUnicode: Boolean
	val allMatch: ((Char) -> Boolean)?
	val firstLetter: (Char) -> Boolean
	val checkingChar: (Char) -> Boolean
	val divider: Char?
	val charStart: (Char) -> String
	val wordBondy: (Char) -> Boolean
	val wordStart: (Char) -> String
	val wordOther: (Char) -> String

	fun validate(source: String): Boolean = source.trim()
		.takeIf { it.isNotBlank() }
		?.let { s ->
			when {
				// first letter should be match
				!firstLetter(s[0]) -> {
					//	println("${s[0]} not match $this first letter rule")
					null
				}
				//if define all match
				allMatch != null && !s.filter(checkingChar).all(allMatch!!) -> {
					//println("$s not match $this allMatch rule ")
					null
				}
				//if no divider it should be all letter or digit
				divider == null && s.find { !it.isLetterOrDigit() } != null -> {
					//println("$s is not all letter Or Digit of $this")
					null
				}
				divider != null && s.find { !it.isLetterOrDigit() && it != divider } != null -> {
					//	println("$s is not all letter Or Digit or $divider of $this")
					null
				}
				//now it is valid
				else -> true
			}
		} != null

	fun format(source: String, from: CaseStyle) =
		from.toWords(source)?.mapIndexed { i, w ->
			buildString {
				if (i == 0) {
					append(charStart(w[0]))
					append(w.subSequence(1 until w.length).map(wordOther).joinToString(""))
				} else {
					append(wordStart(w[0]))
					append(w.subSequence(1 until w.length).map(wordOther).joinToString(""))
				}
			}
		}?.joinToString("")


	fun toWords(source: String): List<String>? = run {
		var lstAscii = true
		source.takeIf { validate(it) }?.trim()?.let {
			when {
				//have divider
				divider != null -> it.split(divider!!)
				//just check word bondy
				else -> it.fold(mutableListOf<MutableList<Char>>())
				{ acc, c ->
					if (wordBondy(c) || (isUnicode && lstAscii && !isAsciiLetter(c))) {
						acc.add(mutableListOf(c))
					} else {
						acc.lastOrNull()?.add(c) ?: acc.add(mutableListOf(c))
					}
					if (isUnicode)
						lstAscii = isAsciiLetter(c)

					acc
				}.map { t -> t.joinToString("") { c -> c.toString() } }
			}
		}
	}
}

private const val space = ' '
private const val dash = '-'
private const val underscore = '_'
private const val CASE_MASK = 0x20
private val isAsciiLower = { c: Char -> c in 'a'..'z' }
private val isAsciiUpper = { c: Char -> c in 'A'..'Z' }
private val isAsciiLetter = { c: Char -> c in 'A'..'Z' || c in 'a'..'z' }
private val toAsciiLower = { c: Char -> if (isAsciiLower(c)) c else c.toInt().xor(CASE_MASK).toChar() }
private val toAsciiUpper = { c: Char -> if (isAsciiUpper(c)) c else c.toInt().xor(CASE_MASK).toChar() }
private fun checkEq(c: Char) = { c1: Char -> c1 == c }
private fun checkIn(c: Char, c2: Char) = { c1: Char -> c1 in c..c2 }
private val checkLetterDigitInAscii = { c: Char -> isAsciiLetter(c) }
private val asIs = { c: Char -> c.toString() }
private val asWith = { pre: Char, case: (Char) -> String -> { c: Char -> pre + case(c) } }
private val asAsciiUpper = { c: Char -> (c.takeIf(isAsciiLower)?.let(toAsciiUpper) ?: c).toString() }
private val asAsciiLower = { c: Char -> (c.takeIf(isAsciiUpper)?.let(toAsciiLower) ?: c).toString() }
private val asUpper = { c: Char -> c.toUpperCase().toString() }
private val asLower = { c: Char -> c.toLowerCase().toString() }
private val isUpper = Char::isUpperCase
private val isLower = Char::isLowerCase

enum class AsciiStyle(
	//default is Pascal
	override val allMatch: ((Char) -> Boolean)? = null,
	override val firstLetter: (Char) -> Boolean = isAsciiUpper,
	override val checkingChar: (Char) -> Boolean = isAsciiLetter,
	override val divider: Char? = null,
	override val charStart: (Char) -> String = asAsciiUpper,
	bondyChecker: (Char) -> Boolean = isAsciiUpper,
	override val wordStart: (Char) -> String = asAsciiUpper,
	override val wordOther: (Char) -> String = asAsciiLower,
	override val isUnicode: Boolean = false
) : CaseStyle {
	Pascal(),
	Camel(
		firstLetter = isAsciiLower,
		charStart = asAsciiLower
	),
	Snake(
		allMatch = isAsciiLower,
		firstLetter = isAsciiLower,
		charStart = asAsciiLower,
		divider = underscore,
		wordStart = asWith(underscore, asAsciiLower),
		wordOther = asAsciiLower
	),
	SnakeUpper(
		allMatch = isAsciiUpper,
		firstLetter = isAsciiUpper,
		charStart = asAsciiUpper,
		divider = underscore,
		wordStart = asWith(underscore, asAsciiUpper),
		wordOther = asAsciiUpper
	),
	Kebab(
		allMatch = isAsciiLower,
		firstLetter = isAsciiLower,
		charStart = asAsciiLower,
		divider = dash,
		wordStart = asWith(dash, asAsciiLower),
		wordOther = asAsciiLower
	),
	KebabUpper(
		allMatch = isAsciiUpper,
		firstLetter = isAsciiUpper,
		charStart = asAsciiUpper,
		divider = dash,
		wordStart = asWith(dash, asAsciiUpper),
		wordOther = asAsciiUpper
	),
	RawLower(
		allMatch = isAsciiLower,
		firstLetter = isAsciiLower,
		charStart = asAsciiLower,
		divider = space,
		wordStart = asWith(space, asAsciiLower),
		wordOther = asAsciiLower
	),
	RawUpper(
		allMatch = isAsciiUpper,
		firstLetter = isAsciiUpper,
		charStart = asAsciiUpper,
		divider = space,
		wordStart = asWith(space, asAsciiUpper),
		wordOther = asAsciiUpper
	),
	Raw(
		divider = space,
		wordStart = asWith(space, asIs),
		wordOther = asIs
	),
	;

	override val wordBondy: (Char) -> Boolean = if (divider != null) checkEq(divider!!) else bondyChecker
}

enum class UnicodeStyle(
	//default is Pascal
	override val allMatch: ((Char) -> Boolean)? = null,
	override val firstLetter: (Char) -> Boolean = isUpper,
	override val checkingChar: (Char) -> Boolean = checkLetterDigitInAscii,
	override val divider: Char? = null,
	override val charStart: (Char) -> String = asUpper,
	bondyChecker: (Char) -> Boolean = isUpper,
	override val wordStart: (Char) -> String = asUpper,
	override val wordOther: (Char) -> String = asLower,
	override val isUnicode: Boolean = true
) : CaseStyle {
	Pascal(),
	Camel(
		firstLetter = isLower,
		charStart = asLower
	),
	Snake(
		allMatch = isLower,
		firstLetter = isLower,
		charStart = asLower,
		divider = underscore,
		wordStart = asWith(underscore, asLower),
		wordOther = asLower
	),
	SnakeUpper(
		allMatch = isUpper,
		firstLetter = isUpper,
		charStart = asUpper,
		divider = underscore,
		wordStart = asWith(underscore, asUpper),
		wordOther = asUpper
	),
	Kebab(
		allMatch = isLower,
		firstLetter = isLower,
		charStart = asLower,
		divider = dash,
		wordStart = asWith(dash, asLower),
		wordOther = asLower
	),
	KebabUpper(
		allMatch = isUpper,
		firstLetter = isUpper,
		charStart = asUpper,
		divider = dash,
		wordStart = asWith(dash, asUpper),
		wordOther = asUpper
	),
	RawLower(
		allMatch = isLower,
		firstLetter = isLower,
		charStart = asLower,
		divider = space,
		wordStart = asWith(space, asLower),
		wordOther = asLower
	),
	RawUpper(
		allMatch = isUpper,
		firstLetter = isUpper,
		charStart = asUpper,
		divider = space,
		wordStart = asWith(space, asUpper),
		wordOther = asUpper
	),
	Raw(
		divider = space,
		wordStart = asWith(space, asIs),
		wordOther = asIs
	),
	;

	override val wordBondy: (Char) -> Boolean = if (divider != null) checkEq(divider!!) else bondyChecker
}
