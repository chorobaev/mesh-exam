package io.flaterlab.meshexam.domain.exam.model

data class AttemptResultModel(
    val examId: String,
    val attemptId: String,
    val examName: String,
    val durationInMillis: Long,
)