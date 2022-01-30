package io.flaterlab.meshexam.domain.model

data class CreateQuestionModel(
    val examId: String,
    val orderNumber: Int,
    val score: Float,
    val type: QuestionType = QuestionType.SINGLE_ANSWER,
)