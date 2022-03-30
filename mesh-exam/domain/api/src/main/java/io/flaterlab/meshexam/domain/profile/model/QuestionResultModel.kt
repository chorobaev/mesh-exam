package io.flaterlab.meshexam.domain.profile.model

data class QuestionResultModel(
    val questionId: String,
    val question: String,
    val answerResultList: List<AnswerResultModel>,
)

data class AnswerResultModel(
    val answerId: String,
    val answer: String,
    val isCorrect: Boolean,
    val isSelected: Boolean,
)