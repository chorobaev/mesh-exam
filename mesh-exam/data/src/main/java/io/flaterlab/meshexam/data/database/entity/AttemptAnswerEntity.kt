package io.flaterlab.meshexam.data.database.entity

import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(tableName = "attempt_answers", indices = [Index(value = ["questionId"], unique = true)])
internal data class AttemptAnswerEntity(
    @PrimaryKey val attemptAnswerId: String,
    val attemptId: String,
    val questionId: String,
    val answerId: String,
    val createdAt: Long,
)