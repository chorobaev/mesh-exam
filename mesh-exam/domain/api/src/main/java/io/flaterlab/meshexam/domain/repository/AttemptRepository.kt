package io.flaterlab.meshexam.domain.repository

import io.flaterlab.meshexam.domain.exam.model.*
import kotlinx.coroutines.flow.Flow

interface AttemptRepository {

    suspend fun attempt(examId: String): String

    fun attemptMetaById(attemptId: String): Flow<AttemptMetaModel>

    suspend fun getActiveAttempts(): List<AttemptMetaModel>

    suspend fun addAttemptAnswer(model: SelectAnswerModel)

    suspend fun finishAttempt(attemptId: String)

    fun selectedAnswerByAttemptAndQuestionId(
        attemptId: String,
        questionId: String
    ): Flow<SelectedAnswerModel>

    fun attemptTimeLeftInSec(attemptId: String): Flow<Int>

    fun attemptResult(attemptId: String): Flow<AttemptResultModel>

    suspend fun sendExamEvent(attemptId: String, event: ExamEvent)
}