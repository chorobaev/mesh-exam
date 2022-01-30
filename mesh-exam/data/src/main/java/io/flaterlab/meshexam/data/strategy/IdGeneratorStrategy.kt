package io.flaterlab.meshexam.data.strategy

import java.util.*

internal fun interface IdGeneratorStrategy {

    fun generate(): String

    companion object {
        operator fun invoke() = IdGeneratorStrategy { UUID.randomUUID().toString() }
    }
}