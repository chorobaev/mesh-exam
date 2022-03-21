package io.flaterlab.meshexam.domain.profile.model

data class HostingResultModel(
    val id: String,
    val studentFullName: String,
    val studentInfo: String,
    val status: String,
    val grade: Int,
    val totalGrade: Int,
)