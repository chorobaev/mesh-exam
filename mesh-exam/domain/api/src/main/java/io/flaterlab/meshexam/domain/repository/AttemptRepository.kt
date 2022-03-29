package io.flaterlab.meshexam.domain.repository

import io.flaterlab.meshexam.domain.exam.model.AttemptMetaModel
import io.flaterlab.meshexam.domain.exam.model.AttemptResultModel
import io.flaterlab.meshexam.domain.exam.model.SelectAnswerModel
import io.flaterlab.meshexam.domain.exam.model.SelectedAnswerModel
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
}