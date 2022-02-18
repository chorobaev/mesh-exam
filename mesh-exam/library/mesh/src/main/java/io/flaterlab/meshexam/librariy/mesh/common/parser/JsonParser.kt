package io.flaterlab.meshexam.librariy.mesh.common.parser

internal interface JsonParser<T> {

    val toJsonParser get() = ::toJson
    val fromJsonParser get() = ::fromJson

    fun fromJson(json: String): T

    fun toJson(model: T): String

    fun interface ToJsonParser<T> {

        operator fun invoke(model: T): String
    }

    fun interface FromJsonParser<T> {

        operator fun invoke(json: String): T
    }
}