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
 *   @File: LZW.kt
 *   @Author:  lcz20@163.com
 *   @LastModified:  2020-04-11 21:34:14
 */

package cn.zenliu.kotlin.util.compress

object LZW {
	/** Compress a string to a list of output symbols. */
	fun compressASCII(uncompressed: String): MutableList<Int> {
		// Build the dictionary.
		var dictSize = 256
		val dictionary = mutableMapOf<String, Int>()
		(0 until dictSize).forEach { dictionary[it.toChar().toString()] = it }
		var w = ""
		val result = mutableListOf<Int>()
		uncompressed.forEach { c ->
			val wc = w + c
			w = if (dictionary.containsKey(wc))
				wc
			else {
				result.add(dictionary[w]!!)
				// Add wc to the dictionary.
				dictionary[wc] = dictSize++
				c.toString()
			}
		}
		// Output the code for w
		if (w.isNotEmpty()) result.add(dictionary[w]!!)
		return result
	}

	fun decompressASCII(compressed: MutableList<Int>): String {
		// Build the dictionary.
		var dictSize = 256
		val dictionary = mutableMapOf<Int, String>()
		(0 until dictSize).forEach { dictionary[it] = it.toChar().toString() }

		var w = compressed.removeAt(0).toChar().toString()
		val result = StringBuilder(w)
		compressed.forEach { k ->
			val entry = when {
				dictionary.containsKey(k) -> dictionary[k]!!
				k == dictSize -> w + w[0]
				else -> throw IllegalArgumentException("Bad compressed k: $k")
			}
			result.append(entry)
			// Add w + entry[0] to the dictionary.
			dictionary[dictSize++] = w + entry[0]
			w = entry
		}
		return result.toString()
	}
}
