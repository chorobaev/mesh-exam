package io.flaterlab.meshexam.data.database.entity.update

import io.flaterlab.meshexam.data.database.entity.AttemptEntity

internal data class AttemptFinishing(
    val attemptId: String,
    val status: AttemptEntity.Status,
    val score: Int,
    val submittedAt: Long,
)