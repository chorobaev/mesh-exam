package io.flaterlab.meshexam.domain.exeption

class MessageException(
    val msg: String? = null,
    val stringResKey: String? = null,
) : Exception()