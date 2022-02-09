package io.flaterlab.meshexam.domain.create.model

data class CreateQuestionModel(
    val examId: String,
    val orderNumber: Int,
    val score: Float,
    val type: QuestionType = QuestionType.SINGLE_ANSWER,
)