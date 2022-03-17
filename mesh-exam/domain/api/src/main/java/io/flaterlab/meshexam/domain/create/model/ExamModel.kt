package io.flaterlab.meshexam.domain.create.model

data class ExamModel(
    val id: String,
    val name: String,
    val type: String,
    val durationInMin: Int,
)