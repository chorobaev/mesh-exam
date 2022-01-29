package io.flaterlab.meshexam.domain.api.model

data class ExamModel(
    val id: String,
    val name: String,
    val type: String,
    val durationInMin: Long,
)