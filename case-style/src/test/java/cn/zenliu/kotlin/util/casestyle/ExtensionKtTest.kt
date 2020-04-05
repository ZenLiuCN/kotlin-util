/*
 *     Copyright (c) 2020.  Zen.Liu All Rights Reserved.
 *     @Project: kotlin-utils
 *     @Module: case-style
 *     @File: ExtensionKtTest.kt
 *     @Author:  lcz20@163.com
 *     @LastModified:  2020-04-05 15:51:24
 */

package cn.zenliu.kotlin.util.casestyle

import org.junit.jupiter.api.*
import org.junit.jupiter.api.Assertions.*
import kotlin.system.*

internal class ExtensionKtTest {
	val pascal = "Alpha中国China"
	val camel = "alpha中国China"
	val kabab = "alpha-中国-china"
	val kababUpper = "ALPHA-中国-CHINA"
	val snake = "alpha_中国_china"
	val snakeUpper = "ALPHA_中国_CHINA"

	@Test
	fun guessStyle() {
		pascal.guessStyle().also {
			assertEquals(AsciiStyle.Pascal, it)
		}
		camel.guessStyle().also {
			assertEquals(AsciiStyle.Camel, it)
		}
		kabab.guessStyle().also {
			assertEquals(AsciiStyle.Kebab, it)
		}
		kababUpper.guessStyle().also {
			assertEquals(AsciiStyle.KebabUpper, it)
		}
		snake.guessStyle().also {
			assertEquals(AsciiStyle.Snake, it)
		}
		snakeUpper.guessStyle().also {
			assertEquals(AsciiStyle.SnakeUpper, it)
		}
	}

	@Test
	fun guessStyleUnicode() {
		pascal.guessStyle(true).also {
			assertEquals(UnicodeStyle.Pascal, it)
		}
		camel.guessStyle(true).also {
			assertEquals(UnicodeStyle.Camel, it)
		}
		kabab.guessStyle(true).also {
			assertEquals(UnicodeStyle.Kebab, it)
		}
		kababUpper.guessStyle(true).also {
			assertEquals(UnicodeStyle.KebabUpper, it)
		}
		snake.guessStyle(true).also {
			assertEquals(UnicodeStyle.Snake, it)
		}
		snakeUpper.guessStyle(true).also {
			assertEquals(UnicodeStyle.SnakeUpper, it)
		}
	}

	@Test
	fun formatStyle() {
		pascal.toCamel().apply { assertEquals(camel, this) }
		kabab.toCamel().apply { assertEquals(camel, this) }
		kababUpper.toCamel().apply { assertEquals(camel, this) }
		snakeUpper.toCamel().apply { assertEquals(camel, this) }
		snake.toCamel().apply { assertEquals(camel, this) }

		camel.toPascal().apply { assertEquals(pascal, this) }
		kabab.toPascal().apply { assertEquals(pascal, this) }
		kababUpper.toPascal().apply { assertEquals(pascal, this) }
		snakeUpper.toPascal().apply { assertEquals(pascal, this) }
		snake.toPascal().apply { assertEquals(pascal, this) }

		pascal.toKebab().apply { assertEquals("alpha中国-china", this) }
		camel.toKebab().apply { assertEquals("alpha中国-china", this) }
		kababUpper.toKebab().apply { assertEquals(kabab, this) }
		snakeUpper.toKebab().apply { assertEquals(kabab, this) }
		snake.toKebab().apply { assertEquals(kabab, this) }

		pascal.toKebabUpper().apply { assertEquals("ALPHA中国-CHINA", this) }
		camel.toKebabUpper().apply { assertEquals("ALPHA中国-CHINA", this) }
		kabab.toKebabUpper().apply { assertEquals(kababUpper, this) }
		snakeUpper.toKebabUpper().apply { assertEquals(kababUpper, this) }
		snake.toKebabUpper().apply { assertEquals(kababUpper, this) }

		pascal.toSnake().apply { assertEquals("alpha中国_china", this) }
		camel.toSnake().apply { assertEquals("alpha中国_china", this) }
		kabab.toSnake().apply { assertEquals(snake, this) }
		kababUpper.toSnake().apply { assertEquals(snake, this) }
		snakeUpper.toSnake().apply { assertEquals(snake, this) }

		pascal.toSnakeUpper().apply { assertEquals("ALPHA中国_CHINA", this) }
		camel.toSnakeUpper().apply { assertEquals("ALPHA中国_CHINA", this) }
		kabab.toSnakeUpper().apply { assertEquals(snakeUpper, this) }
		kababUpper.toSnakeUpper().apply { assertEquals(snakeUpper, this) }
		snake.toSnakeUpper().apply { assertEquals(snakeUpper, this) }
	}

	@Test
	fun formatStyleUnicode() {
		pascal.toCamel(true).apply { assertEquals(camel, this) }
		kabab.toCamel(true).apply { assertEquals(camel, this) }
		kababUpper.toCamel(true).apply { assertEquals(camel, this) }
		snakeUpper.toCamel(true).apply { assertEquals(camel, this) }
		snake.toCamel(true).apply { assertEquals(camel, this) }

		camel.toPascal(true).apply { assertEquals(pascal, this) }
		kabab.toPascal(true).apply { assertEquals(pascal, this) }
		kababUpper.toPascal(true).apply { assertEquals(pascal, this) }
		snakeUpper.toPascal(true).apply { assertEquals(pascal, this) }
		snake.toPascal(true).apply { assertEquals(pascal, this) }

		pascal.toKebab(true).apply { assertEquals(kabab, this) }
		camel.toKebab(true).apply { assertEquals(kabab, this) }
		kababUpper.toKebab(true).apply { assertEquals(kabab, this) }
		snakeUpper.toKebab(true).apply { assertEquals(kabab, this) }
		snake.toKebab(true).apply { assertEquals(kabab, this) }

		pascal.toKebabUpper(true).apply { assertEquals(kababUpper, this) }
		camel.toKebabUpper(true).apply { assertEquals(kababUpper, this) }
		kabab.toKebabUpper(true).apply { assertEquals(kababUpper, this) }
		snakeUpper.toKebabUpper(true).apply { assertEquals(kababUpper, this) }
		snake.toKebabUpper(true).apply { assertEquals(kababUpper, this) }

		pascal.toSnake(true).apply { assertEquals(snake, this) }
		camel.toSnake(true).apply { assertEquals(snake, this) }
		kabab.toSnake(true).apply { assertEquals(snake, this) }
		kababUpper.toSnake(true).apply { assertEquals(snake, this) }
		snakeUpper.toSnake(true).apply { assertEquals(snake, this) }

		pascal.toSnakeUpper(true).apply { assertEquals(snakeUpper, this) }
		camel.toSnakeUpper(true).apply { assertEquals(snakeUpper, this) }
		kabab.toSnakeUpper(true).apply { assertEquals(snakeUpper, this) }
		kababUpper.toSnakeUpper(true).apply { assertEquals(snakeUpper, this) }
		snake.toSnakeUpper(true).apply { assertEquals(snakeUpper, this) }
	}

	@Test
	fun benchMark() {
		(0L..999L).sumBy {
			measureNanoTime {
				formatStyleUnicode()
			}.toInt()
		}.apply {
			println("Unicode transform ${(this / (1000 * 30)) / 1000.0} ms/op")
		}
		(0L..999L).sumBy {
			measureNanoTime {
				formatStyle()
			}.toInt()
		}.apply {
			println("Ascii transform ${(this / (1000 * 30)) / 1000.0} ms/op")
		}
		(0L..999L).sumBy {
			measureNanoTime {
				guessStyleUnicode()
			}.toInt()
		}.apply {
			println("Unicode guess ${(this / (1000 * 6)) / 1000.0} ms/op")
		}
		(0L..999L).sumBy {
			measureNanoTime {
				guessStyle()
			}.toInt()
		}.apply {
			println("Ascii guess ${(this / (1000 * 6)) / 1000.0} ms/op")
		}
	}
}
