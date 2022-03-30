package io.flaterlab.meshexam.data.database.entity.host

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "host_attempt_answers")
internal data class HostAttemptAnswerEntity(
    @PrimaryKey val attemptAnswerId: String,
    val attemptId: String,
    val questionId: String,
    val answerId: String,
    val createdAt: Long,
)