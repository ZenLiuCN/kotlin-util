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
 *   @Module: nquad
 *   @File: NamespaceRegistry.kt
 *   @Author:  lcz20@163.com
 *   @LastModified:  2020-04-11 16:23:49
 */

package cn.zenliu.kotlin.util.nquad.quad.namespace

import cn.zenliu.kotlin.util.nquad.quad.schema.*
import cn.zenliu.kotlin.util.nquad.quad.value.*

open class NamespaceRegistryImpl(
	private val threadSafe: Boolean,
	override val registry: MutableSet<Namespace> = mutableSetOf()
) : NamespaceRegistry<VIRI> {
	private fun <T> doSafe(act: MutableSet<Namespace>.() -> T) = if (threadSafe) {
		synchronized(registry) {
			act.invoke(registry)
		}
	} else act.invoke(registry)

	override fun register(ns: Namespace): Boolean = doSafe {
		add(ns)
	}

	override fun register(prefix: String, namespace: String): Boolean = doSafe {
		add(InternalNamespace(prefix, namespace))
	}

	override fun shortIRIString(iri: String): String = doSafe {
		find { iri.startsWith(it.namespace) }
			?.let {
				iri.replace(it.namespace, it.prefix)
			} ?: iri
	}

	override fun shortIRI(iri: String): VIRI = VIRI(shortIRIString(iri))

	override fun fullIRIString(iri: String): String = doSafe {
		find { iri.startsWith(it.namespace) }
			?.let { "${it.namespace}${iri.removePrefix(it.prefix)}" }
			?: iri
	}

	override fun fullIRI(iri: String): VIRI = VIRI(fullIRIString(iri))

	override fun clone(): NamespaceRegistry<VIRI> = NamespaceRegistryImpl(threadSafe, mutableSetOf<Namespace>().apply {
		addAll(registry)
	})
}
