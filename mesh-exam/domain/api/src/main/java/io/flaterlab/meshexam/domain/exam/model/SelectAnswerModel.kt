package io.flaterlab.meshexam.domain.exam.model

data class SelectAnswerModel(
    val attemptId: String,
    val questionId: String,
    val answerId: String,
)