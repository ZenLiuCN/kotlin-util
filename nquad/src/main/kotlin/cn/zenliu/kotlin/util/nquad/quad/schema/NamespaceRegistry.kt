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

package cn.zenliu.kotlin.util.nquad.quad.schema

/**
 *
 * @param out IRI : Value<*> IRI type
 * @property registry Collection<Namespace> namespace in registry
 */
interface NamespaceRegistry<out IRI : Value<*>> {

	val registry: Collection<Namespace>

	/**
	 * register new namespace
	 * @param ns Namespace
	 * @return Boolean
	 */
	fun register(ns: Namespace): Boolean

	/**
	 * register new namespace by prefix and namespace name
	 * @param prefix String
	 * @param namespace String
	 * @return Boolean
	 */
	fun register(prefix: String, namespace: String): Boolean

	/**
	 * shortIRIString
	 * @param iri String "http://www.w3.org/1999/02/22-rdf-syntax-ns#type"
	 * @return String  eg:"rdf:type"
	 */
	fun shortIRIString(iri: String): String

	/**
	 * shortIRI
	 * @param iri String "http://www.w3.org/1999/02/22-rdf-syntax-ns#type"
	 * @return IRI  eg:"rdf:type"
	 */
	fun shortIRI(iri: String): IRI

	/**
	 * fullIRIString
	 * @param iri String "rdf:type"
	 * @return String "http://www.w3.org/1999/02/22-rdf-syntax-ns#type"
	 */
	fun fullIRIString(iri: String): String

	/**
	 * convert short iri to full iri
	 * @param iri String "rdf:type"
	 * @return IRI "http://www.w3.org/1999/02/22-rdf-syntax-ns#type"
	 */
	fun fullIRI(iri: String): IRI

	/**
	 * full copy of this Registry
	 * @return NamespaceRegistry<IRI>
	 */
	fun clone(): NamespaceRegistry<IRI>
}
