package io.flaterlab.meshexam.data.worker.entity

data class AnswerJson(
    val answer: String,
    val isCorrect: Boolean,
    val orderNumber: Int
)