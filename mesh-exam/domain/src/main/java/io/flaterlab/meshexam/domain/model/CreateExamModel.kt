package io.flaterlab.meshexam.domain.api.model

data class CreateExamModel(
    val name: String,
    val type: String?,
    val durationInMin: Long,
)