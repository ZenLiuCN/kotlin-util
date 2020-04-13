package cn.zenliu.kotlin.util.hash.xxhash

object XXHashFactory {
	fun new(seed: Int = 0, strong: Boolean): XXHash = XXHash32(seed)

}