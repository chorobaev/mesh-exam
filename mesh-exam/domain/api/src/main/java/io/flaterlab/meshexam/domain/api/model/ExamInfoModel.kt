package io.flaterlab.meshexam.domain.api.model

data class ExamInfoModel(
    val id: Long,
    val name: String,
    val host: String,
    val duration: Long,
)