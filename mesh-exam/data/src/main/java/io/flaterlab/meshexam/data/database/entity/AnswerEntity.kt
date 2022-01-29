package io.flaterlab.meshexam.data.database.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "answers")
internal data class AnswerEntity(
    @PrimaryKey val answerId: String,
    val hostQuestionId: String,
    val answer: String,
    val orderNumber: Int,
    val isCorrect: Boolean,
    val createdAt: Long,
    val updatedAt: Long,
)