package cn.zenliu.kotlin.util.hash

import org.junit.jupiter.api.Test
import kotlin.system.measureNanoTime

internal class EndianReaderTest {
	val e = byteArrayOf(0x66, 0x44, 0x54, 0x22, 0x79, 0x22, 0x79)

	@Test
	fun benchmark() {
		assert(EndianReader.longFromLittleEndian(e, 0, 7) == EndianReader.longFromLittleEndianV2(e, 0))
		measureNanoTime {
			(0..100000).forEach { _ ->
				EndianReader.longFromLittleEndian(e, 0, 7)
			}
		}.apply {
			println("v1 ${(this / 100000) / 1000.0} ms/op")
		}
		measureNanoTime {
			(0..100000).forEach { _ ->
				EndianReader.longFromLittleEndianV2(e, 0)
			}
		}.apply {
			println("v1.2 ${(this / 100000) / 1000.0} ms/op")
		}
		assert(EndianReader.intFromLittleEndian(e) == EndianReader.intFromLittleEndianV2(e))
		measureNanoTime {
			(0..100000).forEach { _ ->
				EndianReader.intFromLittleEndian(e)
			}
		}.apply {
			println("int v1 ${(this / 100000) / 1000.0} ms/op")
		}
		measureNanoTime {
			(0..100000).forEach { _ ->
				EndianReader.intFromLittleEndianV2(e)
			}
		}.apply {
			println("int v2 ${(this / 100000) / 1000.0} ms/op")
		}
	}
}