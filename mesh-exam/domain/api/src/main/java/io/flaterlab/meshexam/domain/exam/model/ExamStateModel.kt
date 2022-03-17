package io.flaterlab.meshexam.domain.exam.model

sealed class ExamStateModel {

    object None : ExamStateModel()

    data class Started(
        val examId: String,
        val attemptId: String,
    ) : ExamStateModel()

    data class Waiting(
        val examId: String,
        val examName: String,
    ) : ExamStateModel()
}