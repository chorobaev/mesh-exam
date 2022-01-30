package io.flaterlab.meshexam.domain.create.model

data class AnswerModel(
    val id: String,
    val content: String,
    val isCorrect: Boolean,
)