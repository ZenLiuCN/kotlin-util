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
 *   @Module: delegate
 *   @File: NoneStrongReference.kt
 *   @Author:  lcz20@163.com
 *   @LastModified:  2020-04-23 21:29:15
 */
@file:Suppress("NOTHING_TO_INLINE")

package cn.zenliu.kotlin.util.delegate

import java.lang.ref.SoftReference
import java.lang.ref.WeakReference

import kotlin.reflect.KProperty

@Suppress("HardCodedStringLiteral")
private const val LazyNotInitialized = "Lazy value not initialized yet."

class WeakRef<T>(private var _value: WeakReference<T?>) {
	operator fun getValue(thisRef: Any?, property: KProperty<*>): T? {
		return _value.get()
	}

	operator fun setValue(
		thisRef: Any?, property: KProperty<*>, value: T?
	) {
		_value = WeakReference(value)
	}
}

class SoftRef<T>(private var _value: SoftReference<T?>) {
	operator fun getValue(thisRef: Any?, property: KProperty<*>): T? {
		return _value.get()
	}

	operator fun setValue(
		thisRef: Any?, property: KProperty<*>, value: T?
	) {
		_value = SoftReference(value)
	}
}

private class LazySoftRef<out T>(private val initializer: () -> T) : Lazy<T> {
	private var _value: SoftReference<T>? = null
	override val value: T
		get() {
			if (_value == null) {
				_value = SoftReference(initializer())
			}
			return _value!!.get() ?: SoftReference(initializer()).apply { _value = this }.get()!!
		}

	override fun isInitialized(): Boolean = _value != null
	override fun toString(): String = if (isInitialized()) value.toString() else LazyNotInitialized
}

private class LazyWeakRef<out T>(private val initializer: () -> T) : Lazy<T> {
	private var _value: WeakReference<T>? = null
	override val value: T
		get() {
			if (_value == null) {
				_value = WeakReference(initializer())
			}
			return _value!!.get() ?: WeakReference(initializer()).apply { _value = this }.get()!!
		}

	override fun isInitialized(): Boolean = _value != null
	override fun toString(): String = if (isInitialized()) value.toString() else LazyNotInitialized
}

private class SynchronizedLazySoftRef<out T>(private val initializer: () -> T, lock: Any? = null) : Lazy<T> {
	@Volatile
	private var _value: SoftReference<T>? = null
	private val lock = lock ?: this
	override val value: T
		get() {
			val _v1 = _value
			if (_v1?.get() != null) {
				return _v1.get()!!
			}

			return synchronized(lock) {
				val _v2 = _value
				if (_v2?.get() != null) {
					_v2.get()!!
				} else {
					val typedValue = initializer()
					_value = SoftReference(typedValue)
					typedValue
				}
			}
		}

	override fun isInitialized(): Boolean = _value !== null
	override fun toString(): String = if (isInitialized()) value.toString() else LazyNotInitialized
}

private class SynchronizedLazyWeakRef<out T>(private val initializer: () -> T, lock: Any? = null) : Lazy<T> {
	@Volatile
	private var _value: WeakReference<T>? = null
	private val lock = lock ?: this
	override val value: T
		get() {
			val _v1 = _value
			if (_v1?.get() != null) {
				return _v1.get()!!
			}

			return synchronized(lock) {
				val _v2 = _value
				if (_v2?.get() != null) {
					_v2.get()!!
				} else {
					val typedValue = initializer()
					_value = WeakReference(typedValue)
					typedValue
				}
			}
		}

	override fun isInitialized(): Boolean = _value !== null
	override fun toString(): String = if (isInitialized()) value.toString() else LazyNotInitialized
}

inline fun <T> weakRef(value: T): WeakRef<T> = WeakRef<T>(WeakReference(value))
inline fun <T> softRef(value: T): SoftRef<T> = SoftRef<T>(SoftReference(value))


fun <T> lazySoft(mode: LazyThreadSafetyMode = LazyThreadSafetyMode.SYNCHRONIZED, initializer: () -> T): Lazy<T> =
	when (mode) {
		LazyThreadSafetyMode.SYNCHRONIZED -> SynchronizedLazyWeakRef(initializer)
		LazyThreadSafetyMode.PUBLICATION -> throw NotImplementedError("PUBLICATION mode is not supported by " +
			"WeakReference")
		LazyThreadSafetyMode.NONE -> LazyWeakRef(initializer)
	}

fun <T> lazyWeak(mode: LazyThreadSafetyMode, initializer: () -> T): Lazy<T> =
	when (mode) {
		LazyThreadSafetyMode.SYNCHRONIZED -> SynchronizedLazySoftRef(initializer)
		LazyThreadSafetyMode.PUBLICATION -> throw NotImplementedError("PUBLICATION mode is not supported by " +
			"SoftReference")
		LazyThreadSafetyMode.NONE -> LazySoftRef(initializer)
	}