package io.flaterlab.meshexam.domain.profile.model

data class IndividualResultModel(
    val examId: String,
    val examName: String,
    val examInfo: String,
    val durationInMillis: Long,
    val totalQuestionsCount: Int,
    val correctAnswers: Int,
    val questionInfoList: List<QuestionResultInfoModel>,
)

data class QuestionResultInfoModel(
    val questionId: String,
    val isCorrect: Boolean,
)