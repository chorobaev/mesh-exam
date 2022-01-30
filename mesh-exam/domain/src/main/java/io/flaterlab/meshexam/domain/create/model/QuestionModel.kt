package io.flaterlab.meshexam.domain.create.model

data class QuestionModel(
    val id: String,
    val content: String,
    val orderNumber: Int,
    val type: QuestionType,
    val score: Float,
)