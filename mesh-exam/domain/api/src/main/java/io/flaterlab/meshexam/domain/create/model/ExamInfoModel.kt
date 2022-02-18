package io.flaterlab.meshexam.domain.create.model

data class ExamInfoModel(
    val id: String,
    val name: String,
    val host: String,
    val duration: Long,
)