package io.flaterlab.meshexam.data.database.entity.update

data class AttemptFinishing(
    val attemptId: String,
    val score: Int,
    val submittedAt: Long,
)