package cn.zenliu.kotlin.util.hash.xxhash

import cn.zenliu.kotlin.util.hash.EndianReader.Companion.longFromLittleEndian
import java.lang.Integer.rotateLeft


internal class XXHash32 @JvmOverloads constructor(private val seed: Int = 0) : XXHash {
	init {
		initializeState()
	}

	private val oneByte = ByteArray(1)
	private val state = IntArray(4)
	private val buffer = ByteArray(BUF_SIZE)
	private var totalLen = 0
	private var pos = 0
	override fun reset() {
		initializeState()
		totalLen = 0
		pos = 0
	}

	override fun update(b: Int) {
		oneByte[0] = (b and 0xff).toByte()
		update(oneByte, 0, 1)
	}

	override fun update(b: ByteArray, offset: Int, len: Int) {
		var off = offset
		if (len <= 0) {
			return
		}
		totalLen += len
		val end = off + len
		if (pos + len < BUF_SIZE) {
			System.arraycopy(b, off, buffer, pos, len)
			pos += len
			return
		}
		if (pos > 0) {
			val size = BUF_SIZE - pos
			System.arraycopy(b, off, buffer, pos, size)
			process(buffer, 0)
			off += size
		}
		val limit = end - BUF_SIZE
		while (off <= limit) {
			process(b, off)
			off += BUF_SIZE
		}
		if (off < end) {
			pos = end - off
			System.arraycopy(b, off, buffer, 0, pos)
		}
	}

	override fun getValue(): Long {
		var hash = if (totalLen > BUF_SIZE) {
			rotateLeft(state[0], 1) +
				rotateLeft(state[1], 7) +
				rotateLeft(state[2], 12) +
				rotateLeft(state[3], 18)
		} else {
			state[2] + PRIME5
		}
		hash += totalLen
		var idx = 0
		val limit = pos - 4
		while (idx <= limit) {
			hash = rotateLeft(hash + buffer.getInt(idx) * PRIME3, 17) * PRIME4
			idx += 4
		}
		while (idx < pos) {
			hash = rotateLeft(hash + (buffer[idx++].toInt() and 0xff) * PRIME5, 11) * PRIME1
		}
		hash = hash xor (hash ushr 15)
		hash *= PRIME2
		hash = hash xor (hash ushr 13)
		hash *= PRIME3
		hash = hash xor (hash ushr 16)
		return (hash.toLong() and 0xffffffffL)
	}

	private fun initializeState() {
		state[0] = seed + PRIME1 + PRIME2
		state[1] = seed + PRIME2
		state[2] = seed
		state[3] = seed - PRIME1
	}

	private fun process(b: ByteArray, offset: Int) {
		var s0 = state[0]
		var s1 = state[1]
		var s2 = state[2]
		var s3 = state[3]
		s0 = rotateLeft(s0 + b.getInt(offset) * PRIME2, ROTATE_BITS) * PRIME1
		s1 = rotateLeft(s1 + b.getInt(offset + 4) * PRIME2, ROTATE_BITS) * PRIME1
		s2 = rotateLeft(s2 + b.getInt(offset + 8) * PRIME2, ROTATE_BITS) * PRIME1
		s3 = rotateLeft(s3 + b.getInt(offset + 12) * PRIME2, ROTATE_BITS) * PRIME1
		state[0] = s0
		state[1] = s1
		state[2] = s2
		state[3] = s3
		pos = 0
	}

	companion object {
		private const val BUF_SIZE = 16
		private const val ROTATE_BITS = 13
		private const val PRIME1 = 2654435761L.toInt()
		private const val PRIME2 = 2246822519L.toInt()
		private const val PRIME3 = 3266489917L.toInt()
		private const val PRIME4 = 668265263
		private const val PRIME5 = 374761393
		private fun ByteArray.getInt(idx: Int): Int {
			return (longFromLittleEndian(this, idx, 4) and 0xffffffffL).toInt()
		}
	}

}
