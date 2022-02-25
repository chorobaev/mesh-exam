package io.flaterlab.meshexam.domain.exam.model

data class ExamAnswerModel(
    val id: String,
    val content: String,
    val isSelected: Boolean,
)