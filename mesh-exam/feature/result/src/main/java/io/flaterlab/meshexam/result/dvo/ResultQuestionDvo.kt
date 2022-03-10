package io.flaterlab.meshexam.result.dvo

internal data class ResultQuestionDvo(
    val questionId: String,
    val question: String,
    val answers: List<ResultAnswerDvo>,
)

internal data class ResultAnswerDvo(
    val answerId: String,
    val answer: String,
    val isCorrect: Boolean,
    val isSelected: Boolean,
)