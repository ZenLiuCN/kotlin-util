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
 *   @Module: zipo
 *   @File: XXHash.kt
 *   @Author:  lcz20@163.com
 *   @LastModified:  2020-04-08 01:08:07
 */

package cn.zenliu.kotlin.util.zipo


/**
 * xxHash in kotlin
 */
object XXHash {
	private const val PRIME1 = 2654435761L.toInt()
	private const val PRIME2 = 2246822519L.toInt()
	private const val PRIME3 = 3266489917L.toInt()
	private const val PRIME4 = 668265263
	private const val PRIME5 = 0x165667b1

	private object Hasher {
		private var len: Int = 0
		private var v1 = PRIME1
		private var v2 = PRIME2
		private var v3 = PRIME3
		private var v4 = PRIME4
		private fun digest32(er: EndianReader, seed: Int, initializer: (limit: Int) -> Unit): Int {
			len = er.size
			if (len < 16) {
				return digestSmall(er, seed)
			}
			var i = len - 16
			v1 = seed + PRIME1
			v2 = v1 * PRIME2 + len
			v3 = v2 * PRIME3
			v4 = v3 * PRIME4
			initializer(len - 16)
			v1 += Integer.rotateLeft(v1, 17)
			v2 += Integer.rotateLeft(v2, 19)
			v3 += Integer.rotateLeft(v3, 13)
			v4 += Integer.rotateLeft(v4, 11)
			v1 *= PRIME1
			v2 *= PRIME1
			v3 *= PRIME1
			v4 *= PRIME1
			v1 += er.getInt(i)
			i += 4
			v2 += er.getInt(i)
			i += 4
			v3 += er.getInt(i)
			i += 4
			v4 += er.getInt(i)
			v1 *= PRIME2
			v2 *= PRIME2
			v3 *= PRIME2
			v4 *= PRIME2
			v1 += Integer.rotateLeft(v1, 11)
			v2 += Integer.rotateLeft(v2, 17)
			v3 += Integer.rotateLeft(v3, 19)
			v4 += Integer.rotateLeft(v4, 13)
			v1 *= PRIME3
			v2 *= PRIME3
			v3 *= PRIME3
			v4 *= PRIME3
			var crc = v1 + Integer.rotateLeft(v2, 3) + Integer.rotateLeft(v3, 6) + Integer.rotateLeft(v4, 9)
			crc = crc xor (crc ushr 11)
			crc += (PRIME4 + len) * PRIME1
			crc = crc xor (crc ushr 15)
			crc *= PRIME2
			crc = crc xor (crc ushr 13)
			return crc
		}

		private fun digestSmall(er: EndianReader, seed: Int): Int {
			len = er.size
			val limit = len - 4
			var idx = seed + PRIME1
			var crc = PRIME5
			var i = 0
			while (i < limit) {
				crc += er.getInt(i) + idx++
				crc += Integer.rotateLeft(crc, 17) * PRIME4
				crc *= PRIME1
				i += 4
			}
			while (i < len) {
				crc += (er[i].toInt() and 0xFF) + idx++
				crc *= PRIME1
				i++
			}
			crc += len
			crc = crc xor (crc ushr 15)
			crc *= PRIME2
			crc = crc xor (crc ushr 13)
			crc *= PRIME3
			crc = crc xor (crc ushr 16)
			return crc
		}

		fun digestFast32(er: EndianReader, seed: Int): Int = digest32(er, seed) {
			var i = 0
			while (i < it) {
				v1 = Integer.rotateLeft(v1, 13) + er.getInt(i)
				i += 4
				v2 = Integer.rotateLeft(v2, 11) + er.getInt(i)
				i += 4
				v3 = Integer.rotateLeft(v3, 17) + er.getInt(i)
				i += 4
				v4 = Integer.rotateLeft(v4, 19) + er.getInt(i)
				i += 4
			}
		}

		fun digestStrong32(er: EndianReader, seed: Int): Int = digest32(er, seed) {
			var i = 0
			while (i < it) {
				v1 += Integer.rotateLeft(v1, 13) * PRIME1 + er.getInt(i)
				i += 4
				v2 += Integer.rotateLeft(v2, 11) * PRIME1 + er.getInt(i)
				i += 4
				v3 += Integer.rotateLeft(v3, 17) * PRIME1 + er.getInt(i)
				i += 4
				v4 += Integer.rotateLeft(v4, 19) * PRIME1 + er.getInt(i)
				i += 4
			}
		}
	}

	fun digestFast32(data: ByteArray, seed: Int, bigEndian: Boolean): Int =
		Hasher.digestFast32(if (bigEndian) EndianReader.BigEndianReader(data) else EndianReader.LittleEndianReader(data), seed)

	fun digestStrong32(data: ByteArray, seed: Int, bigEndian: Boolean): Int =
		Hasher.digestStrong32(if (bigEndian) EndianReader.BigEndianReader(data) else EndianReader.LittleEndianReader(data), seed)
}
