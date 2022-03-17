package io.flaterlab.meshexam.domain.create.model

data class CreateExamModel(
    val name: String,
    val type: String?,
    val durationInMin: Int,
)