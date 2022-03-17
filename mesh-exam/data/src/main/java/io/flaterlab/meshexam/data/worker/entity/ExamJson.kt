package io.flaterlab.meshexam.data.worker.entity

data class ExamJson(
    val durationInMin: Int,
    val name: String,
    val questions: List<QuestionJson>,
    val type: String
)