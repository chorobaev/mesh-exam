package io.flaterlab.meshexam.data.database.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "questions")
internal data class QuestionEntity(
    @PrimaryKey val questionId: String,
    val hostExamId: String,
    val question: String,
    val type: String,
    val score: Float,
    val orderNumber: Int,
    val createdAt: Long,
    val updatedAt: Long,
)