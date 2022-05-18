package io.flaterlab.meshexam.domain.profile.model

data class ExamHistoryModel(
    val id: String,
    val name: String,
    val durationInMillis: Long,
    val isHosting: Boolean,
)