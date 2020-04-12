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
 *   @File: RDFS.kt
 *   @Author:  lcz20@163.com
 *   @LastModified:  2020-04-11 16:23:49
 */

package cn.zenliu.kotlin.util.nquad.quad.namespace

import cn.zenliu.kotlin.util.nquad.quad.*
import cn.zenliu.kotlin.util.nquad.quad.schema.*
import cn.zenliu.kotlin.util.nquad.quad.value.*

object RDFS : Namespace {
	init {
		Global.namespaceRegistry.register(this)
	}

	override val prefix = "rdfs:"
	override val namespace = "http://www.w3.org/2000/01/rdf-schema#"
	// Classes

	// The class resource, everything.
	val Resource = VIRI("${prefix}Resource")

	// The class of classes.
	val Class = VIRI("${prefix}Class")

	// The class of literal values, eg. textual strings and integers.
	val Literal = VIRI("${prefix}Literal")

	// The class of RDF containers.
	val Container = VIRI("${prefix}Container")

	// The class of RDF datatypes.
	val Datatype = VIRI("${prefix}Datatype")

	// The class of container membership properties, rdf:_1, rdf:_2, ..., all of which are sub-properties of 'member'.
	val ContainerMembershipProperty = VIRI("${prefix}ContainerMembershipProperty")

	// Properties

	// The subject is a subclass of a class.
	val SubClassOf = VIRI("${prefix}subClassOf")

	// The subject is a subproperty of a property.
	val SubPropertyOf = VIRI("${prefix}subPropertyOf")

	// A description of the subject resource.
	val Comment = VIRI("${prefix}comment")

	// A human-readable name for the subject.
	val Label = VIRI("${prefix}label")

	// A domain of the subject property.
	val Domain = VIRI("${prefix}domain")

	// A range of the subject property.
	val Range = VIRI("${prefix}range")

	// Further information about the subject resource.
	val SeeAlso = VIRI("${prefix}seeAlso")

	// The defininition of the subject resource.
	val IsDefinedBy = VIRI("${prefix}isDefinedBy")

	// A member of the subject resource.
	val Member = VIRI("${prefix}member")
}
