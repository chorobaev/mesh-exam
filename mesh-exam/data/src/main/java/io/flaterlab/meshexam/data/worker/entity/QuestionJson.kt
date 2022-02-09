package io.flaterlab.meshexam.data.worker.entity

data class QuestionJson(
    val answers: List<AnswerJson>,
    val orderNumber: Int,
    val question: String,
    val score: Float,
    val type: String
)