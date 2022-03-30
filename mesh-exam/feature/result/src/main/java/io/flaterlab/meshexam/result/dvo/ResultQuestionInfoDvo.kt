package io.flaterlab.meshexam.result.dvo

internal data class ResultQuestionInfoDvo(
    val questionId: String,
    val attemptId: String,
    val isCorrect: Boolean,
)