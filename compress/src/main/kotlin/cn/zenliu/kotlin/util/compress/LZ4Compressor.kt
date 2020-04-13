package cn.zenliu.kotlin.util.compress

val LZ4_SIGNATURE = byteArrayOf( //NOSONAR
	4, 0x22, 0x4d, 0x18
)
private val SKIPPABLE_FRAME_TRAILER = byteArrayOf(
	0x2a, 0x4d, 0x18
)
private const val SKIPPABLE_FRAME_PREFIX_BYTE_MASK: Byte = 0x50

const val VERSION_MASK = 0xC0
const val SUPPORTED_VERSION = 0x40
const val BLOCK_INDEPENDENCE_MASK = 0x20
const val BLOCK_CHECKSUM_MASK = 0x10
const val CONTENT_SIZE_MASK = 0x08
const val CONTENT_CHECKSUM_MASK = 0x04
const val BLOCK_MAX_SIZE_MASK = 0x70
const val UNCOMPRESSED_FLAG_MASK = -0x80000000