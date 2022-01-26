package io.flaterlab.meshexam.core

interface Mapper<IN, OUT> {

    operator fun invoke(input: IN): OUT
}