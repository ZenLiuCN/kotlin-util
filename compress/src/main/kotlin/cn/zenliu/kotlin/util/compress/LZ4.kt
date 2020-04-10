package cn.zenliu.kotlin.util.compress


fun Int.combine(i: Int) = run {
	assert(this in 0..15 && i in 0..15)
	shl(4) + i
}

fun Int.high() = shr(4)
fun Int.low() = and(0xf)


fun main() {
	12.combine(15)
		.apply { println(this.toString(2).padStart(8, '0')) }
		.apply { println(this.high()); println(this.low()); }
}