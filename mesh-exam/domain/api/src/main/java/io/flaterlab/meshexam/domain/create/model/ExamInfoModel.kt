package io.flaterlab.meshexam.domain.api.model

data class ExamInfoModel(
    val id: String,
    val name: String,
    val host: String,
    val duration: Long,
)