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
 *   @Module: compress
 *   @File: LZ4String2.kt
 *   @Author:  lcz20@163.com
 *   @LastModified:  2020-04-11 21:09:40
 */

package cn.zenliu.kotlin.util.compress

import kotlin.math.*


object LZ4String2 {
	internal const val keyStr = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789+/="
	internal const val keyStrUri = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789+-$"

	internal data class Data(
		var `val`: Char = '0',
		var position: Int = 0,
		var index: Int = 1
	)

	private fun StringBuilder.charLess256(yes: () -> Unit, no: () -> Unit) {
		if (this[0].toInt() < 256) {
			yes.invoke()
		} else {
			no.invoke()
		}
	}

	private fun Int.power() = 1 shl this

	private inline fun loopBit(numBits: Int, action: () -> Unit) {
		(0 until numBits).forEach { _ ->
			action.invoke()
		}
	}

	private inline fun loop8(action: () -> Unit) {
		(0 until 8).forEach { _ ->
			action.invoke()
		}
	}

	private inline fun loop16(action: () -> Unit) {
		(0 until 16).forEach { _ ->
			action.invoke()
		}
	}

	private fun _compress(source: String, bitsPerChar: Int, charOfInt: (code: Int) -> Char): String {
		var currentChar: String
		var value: Int
		val contextWord: StringBuilder = StringBuilder()
		var wordChain: String
		val contextDictionary = mutableMapOf<String, Int>()
		val dictToCreate = mutableMapOf<String, Boolean>()
		var context_enlargeIn = 2.0 // Compensate for the first entry which should not count
		var currentDictSize = 3
		//output of char
		val output = mutableListOf<Char>()
		var numBits = 2
		var contextDataVal = 0
		var contextDataPosition = 0
		fun minusEnlargeIn() {
			context_enlargeIn--
			if (context_enlargeIn == 0.0) {
				context_enlargeIn = 2.0.pow(numBits.toDouble())
				numBits++
			}
		}


		fun checkPosition() {
			if (contextDataPosition == bitsPerChar - 1) {
				contextDataPosition = 0
				output.add(charOfInt(contextDataVal))
				contextDataVal = 0
			} else {
				contextDataPosition++
			}
		}

		fun processContextWordInCreateDictionary() {
			contextWord.charLess256({
				loopBit(numBits) {
					contextDataVal = contextDataVal shl 1
					checkPosition()
				}
				value = contextWord[0].toInt()
				loop8 {
					contextDataVal = contextDataVal shl 1 or (value and 1)
					checkPosition()
					value = value shr 1

				}
			}) {
				value = 1
				loopBit(numBits) {
					contextDataVal = contextDataVal shl 1 or value
					checkPosition()
					value = 0
				}
				value = contextWord[0].toInt()
				loop16 {
					contextDataVal = contextDataVal shl 1 or (value and 1)
					checkPosition()
					value = value shr 1
				}
			}
			minusEnlargeIn()
			dictToCreate.remove(contextWord.toString())
		}

		fun processContextWord() {
			if (dictToCreate.containsKey(contextWord.toString())) {
				processContextWordInCreateDictionary()
			} else {
				value = contextDictionary[contextWord.toString()]!! //not be empty?
				loopBit(numBits) {
					contextDataVal = contextDataVal shl 1 or (value and 1)
					checkPosition()
					value = value shr 1
				}
			}
			minusEnlargeIn()
		}
		source.forEach {
			currentChar = it.toString()

			if (!contextDictionary.containsKey(currentChar)) {
				//char not in dictionary then put it
				contextDictionary[currentChar] = currentDictSize++
				//this char should create as dict
				dictToCreate[currentChar] = true
			}
			wordChain = contextWord.append(currentChar).toString()
			if (contextDictionary.contains(wordChain)) {
				contextWord.clear()
				contextWord.append(wordChain)
			} else {

				processContextWord()
				// Add wc to the dictionary.
				contextDictionary[wordChain] = currentDictSize++
				contextWord.clear()
				contextWord.append(currentChar)
			}
		}
		// Output the code for w.
		if (contextWord.isNotBlank()) {
			processContextWord()
		}
		// Mark the end of the stream
		value = 2
		loopBit(numBits) {
			contextDataVal = contextDataVal shl 1 or (value and 1)
			if (contextDataPosition == bitsPerChar - 1) {
				contextDataPosition = 0
				output.add(charOfInt(contextDataVal))
				contextDataVal = 0
			} else {
				contextDataPosition++
			}
			value = value shr 1
		}
		// Flush the last char
		while (true) {
			contextDataVal = contextDataVal shl 1
			if (contextDataPosition == bitsPerChar - 1) {
				output.add(charOfInt(contextDataVal))
				break
			} else
				contextDataPosition++
		}
		return output.joinToString("")
	}

	private val Int.string get() = this.toChar().toString()
	private fun _decompress(length: Int, resetValue: Int, getNextValue: (idx: Int) -> Char): String? {
		val builder = StringBuilder()
		val dictionary = mutableListOf<String>(0.string, 1.string, 2.string)
		var bits = 0
		var maxpower = 2.power()
		var power = 1
		var data = Data(getNextValue(0), resetValue, 1)
		var resb: Int
		var c: String = ""
		var w = ""
		var entry = ""
		var numBits = 3
		var enlargeIn = 4
		var dictSize = 4
		var next: Int = 0
		fun doPower(initBits: Int, initPower: Int, initMaxPowerFactor: Int, mode: Int = 0) {
			bits = initBits
			maxpower = initMaxPowerFactor.power()
			power = initPower
			while (power != maxpower) {
				resb = data.`val`.toInt() and data.position
				data.position = data.position shr 1
				if (data.position == 0) {
					data.position = resetValue
					data.`val` = getNextValue(data.index++)
				}
				bits = bits or (if (resb > 0) 1 else 0) * power
				power = power shl 1
			}
			when (mode) {
				0 -> Unit
				1 -> c = bits.string
				2 -> {
					dictionary.add(dictSize++, bits.string)
					next = (dictSize - 1)
					enlargeIn--
				}
			}
		}

		fun checkEnlargeIn() {
			if (enlargeIn == 0) {
				enlargeIn = numBits.power()
				numBits++
			}
		}
		doPower(bits, 1, 2)
		next = bits
		when (next) {
			0 -> doPower(0, 1, 8, 1)
			1 -> doPower(0, 1, 16, 1)
			2 -> return ""
		}
		dictionary.add(3, c)
		w = c
		builder.append(w)
		while (true) {
			if (data.index > length) {
				return ""
			}
			doPower(0, 1, numBits)
			next = bits
			when (next) {
				0 -> doPower(0, 1, 8, 2)
				1 -> doPower(0, 1, 16, 2)
				2 -> return builder.toString()
			}
			checkEnlargeIn()
			if (dictionary.size > next) {
				entry = dictionary[next]
			} else if (next == dictSize) {
				entry = w + w[0]
			} else {
				return null
			}
			builder.append(entry)
			// Add w+entry[0] to the dictionary.
			dictionary.add(dictSize++, w + entry[0])
			enlargeIn--
			w = entry
			checkEnlargeIn()
		}


	}


	fun compress(source: String) = _compress(source, 16) { it.toChar() }
	fun decompres(compressed: String) =
		if (compressed.isBlank()) null else _decompress(compressed.length, 32768) {
			compressed[it]
		}

	fun decompressFromEncodedURIComponent(input: String) =
		when {
			input.isNullOrBlank() -> ""
			else -> {
				input.replace(" ", "+").let {
					_decompress(input.length, 32) {
						keyStrUri.indexOf(input[it]).toChar()
					}
				}
			}
		}

	fun compressToEncodedURIComponent(input: String) = _compress(input, 6) {
		keyStrUri[it]
	}

	fun compressToBase64(input: String): String {
		val res = _compress(input, 6) { keyStr[it] }
		return when (res.length % 4) { // To produce valid Base64
			0 -> res
			1 -> res + "==="
			2 -> res + "=="
			3 -> res + "="
			else -> throw Exception("i do not know what happened") //what !
		}
	}

	fun decompressFromBase64(input: String) = when {
		input.isNullOrBlank() -> null
		else -> _decompress(input.length, 32) {
			keyStr.indexOf(input[it]).toChar()
		}
	}

	fun decompressFromUTF16(input: String) = when {
		input.isBlank() -> null
		else -> _decompress(input.length, 16384) {
			(input[it].toInt() - 32).toChar()
		}
	}

	fun compressToUTF16(input: String) = _compress(input, 15) { (it + 32).toChar() } + " "
}
