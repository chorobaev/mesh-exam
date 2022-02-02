package io.flaterlab.meshexam.core

class Optional<T : Any> private constructor(
    private val reference: T?
) {

    fun isPresent() = reference != null

    fun isNotPresent() = reference == null

    fun get(): T = reference!!

    fun getOrNull() = reference

    fun getOrDefault(def: T) = reference ?: def

    companion object {

        fun <T : Any> empty() = Optional<T>(null)

        fun <T : Any> of(e: T?) = Optional(e)
    }
}