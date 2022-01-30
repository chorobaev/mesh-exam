package io.flaterlab.meshexam.data.database.entity.update

data class AnswerCorrectness(
    val answerId: String,
    val isCorrect: Boolean,
    val updatedAt: Long,
)