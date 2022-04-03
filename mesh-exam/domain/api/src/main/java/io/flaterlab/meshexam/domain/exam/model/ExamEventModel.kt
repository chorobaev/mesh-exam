package io.flaterlab.meshexam.domain.exam.model

import java.util.*

data class ExamEventModel(
    val userId: String,
    val userFullName: String,
    val event: ExamEvent,
    val time: Date,
)