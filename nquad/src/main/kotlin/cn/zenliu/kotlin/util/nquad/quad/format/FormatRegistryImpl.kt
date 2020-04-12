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
 *   @File: FormatRegistryImpl.kt
 *   @Author:  lcz20@163.com
 *   @LastModified:  2020-04-11 16:23:49
 */

package cn.zenliu.kotlin.util.nquad.quad.format

import cn.zenliu.kotlin.util.nquad.quad.schema.*

class FormatRegistryImpl(
	override val registryByName: MutableMap<String, Format> = mutableMapOf(),
	override val registryByExt: MutableMap<String, Format> = mutableMapOf(),
	override val registryByMime: MutableMap<String, Format> = mutableMapOf()
) : FormatRegistry {
	override fun all(): Collection<Format> = mutableSetOf<Format>().apply {
		addAll(registryByExt.values)
		addAll(registryByMime.values)
		addAll(registryByName.values)
	}

	override fun register(fmt: Format) {
		fmt.ext.forEach {
			registryByExt[it] = fmt
		}
		fmt.mime.forEach {
			registryByMime[it] = fmt
		}
		registryByName[fmt.name] = fmt
	}
}
