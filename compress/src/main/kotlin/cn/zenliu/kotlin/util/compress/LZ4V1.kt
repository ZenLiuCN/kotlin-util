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
 *   @File: LZ4V1.kt
 *   @Author:  lcz20@163.com
 *   @LastModified:  2020-04-13 01:13:56
 */

package cn.zenliu.kotlin.util.compress

import java.math.*


object LZ4V1 {
	const val frameMagic: Int = 0x184D2204
	const val frameSkipMagic: Int = 0x184D2A50
	const val minMatch: Int = 4  // the minimum size of the match sequence size (4 bytes)
	const val winSizeLog: Int = 16 // LZ4 64Kb window size limit
	const val winSize: Int = 1 shl winSizeLog
	const val winMask: Int = winSize - 1 // 64Kb window of previous data for dependent blocks
	const val compressedBlockFlag: Int = 1 shl 31
	const val compressedBlockMask: Long = compressedBlockFlag - 1L
	const val hashLog = 16
	const val htSize = 1 shl hashLog
	const val mfLimit = 10 + minMatch // The last match cannot start within the last 14 bytes.
	const val blockSize64K = 1 shl (16 + 2 * 0)
	const val blockSize256K = 1 shl (16 + 2 * 1)
	const val blockSize1M = 1 shl (16 + 2 * 2)
	const val blockSize4M = 1 shl (16 + 2 * 3)

	private class Header(
		val BlockChecksum: Boolean,   // Compressed blocks checksum flag.
		val NoChecksum: Boolean,   // Frame checksum flag.
		val BlockMaxSize: Int,    // Size of the uncompressed data block (one of [64KB, 256KB, 1MB, 4MB]). Default=4MB.
		val Size: BigInteger, // Frame total size. It is _not_ computed by the Writer.
		val CompressionLevel: Int    // Compression level (higher is better, use 0 for fastest compression).
	) {
		var done: Boolean = false   // Header processed flag (Read or Write and checked).
			private set

		fun reset() {
			done = false
		}

	}


}

fun main() {
	println((1 shl 31) - 1L)
}
