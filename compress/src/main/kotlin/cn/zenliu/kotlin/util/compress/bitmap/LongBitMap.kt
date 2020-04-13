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
 *   @File: LongBitMap.kt
 *   @Author:  lcz20@163.com
 *   @LastModified:  2020-04-14 00:13:38
 */

package cn.zenliu.kotlin.util.compress.bitmap


class IntBitArray(size: Int = 32, bits: IntArray? = null) {
	private var bits: IntArray
	var bitSize: Int
		private set
	var intSize: Int
		private set

	init {
		bitSize = if (size < 32) 32 else size
		intSize = (bitSize ushr 5) + 1
		this.bits = bits ?: IntArray(intSize + 1)
	}

	fun size(): Int = bitSize


	fun getBit(bit: Int): Boolean = bits[bit ushr 5] and mask[bit % 32] != 0

	fun getNextBit(startBit: Int): Int {
		var start = startBit
		for (i in (start ushr 5)..intSize) {
			val bits = bits[i]
			if (bits != 0) {
				for (b in start % 32..31) {
					if (bits and mask[b] != 0) {
						return (i shl 5) + b
					}
				}
			}
			start = 0
		}
		return -1
	}


	private var pos = Int.MAX_VALUE
	private var node = 0
	private var integer = 0
	private var bit = 0
	fun getBitNumber(pos: Int): Int {
		if (pos == this.pos) return node
		if (pos < this.pos) {
			this.pos = 0
			bit = this.pos
			integer = bit
		}
		while (integer <= intSize) {
			val bits = bits[integer]
			if (bits != 0) { // Any bits set?
				while (bit < 32) {
					if (bits and mask[bit] != 0) {
						if (++this.pos == pos) {
							node = (integer shl 5) + bit - 1
							return node
						}
					}
					bit++
				}
				bit = 0
			}
			integer++
		}
		return 0
	}

	fun data(): IntArray = bits

	var bitFirst: Int = Int.MAX_VALUE
	var bitLast: Int = Int.MIN_VALUE

	fun setBit(bit: Int) {
		if (bit >= bitSize) return
		val i = bit ushr 5
		if (i < bitFirst) bitFirst = i
		if (i > bitLast) bitLast = i
		bits[i] = bits[i] or mask[bit % 32]
	}

	fun resize(newSize: Int) {
		if (newSize > bitSize) {
			intSize = (newSize ushr 5) + 1
			val newBits = IntArray(intSize + 1)
			bits.copyInto(newBits, 0, 0, (bitSize ushr 5) + 1)
			bits = newBits
			bitSize = newSize
		}
	}

	fun cloneArray(): IntBitArray {
		return IntBitArray(intSize, bits)
	}


	companion object {
		private val mask = intArrayOf(
			-0x80000000,
			0x40000000,
			0x20000000,
			0x10000000,
			0x08000000,
			0x04000000,
			0x02000000,
			0x01000000,
			0x00800000,
			0x00400000,
			0x00200000,
			0x00100000,
			0x00080000,
			0x00040000,
			0x00020000,
			0x00010000,
			0x00008000,
			0x00004000,
			0x00002000,
			0x00001000,
			0x00000800,
			0x00000400,
			0x00000200,
			0x00000100,
			0x00000080,
			0x00000040,
			0x00000020,
			0x00000010,
			0x00000008,
			0x00000004,
			0x00000002,
			0x00000001
		)
	}
}


