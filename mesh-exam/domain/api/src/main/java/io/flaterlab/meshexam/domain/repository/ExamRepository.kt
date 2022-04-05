package io.flaterlab.meshexam.domain.repository

import io.flaterlab.meshexam.domain.create.model.CreateExamModel
import io.flaterlab.meshexam.domain.create.model.ExamModel
import io.flaterlab.meshexam.domain.create.model.*
import kotlinx.coroutines.flow.Flow

interface ExamRepository {

    fun exams(): Flow<List<ExamModel>>

    suspend fun createExam(model: CreateExamModel): String

    suspend fun getExamWithQuestionIdsByExamId(examId: String): ExamWithQuestionIdsModel

    suspend fun getExamByHostingId(hostingId: String): ExamModel


    fun questionById(questionId: String): Flow<QuestionModel>

    suspend fun createQuestion(model: CreateQuestionModel): String

    suspend fun deleteQuestions(vararg questionIds: String)

    suspend fun updateQuestionContent(questionId: String, content: String)


    fun answersByQuestionId(questionId: String): Flow<List<AnswerModel>>

    suspend fun getAnswerById(answerId: String): AnswerModel

    suspend fun createAnswer(model: CreateAnswerModel): String

    suspend fun deleteAnswers(vararg answerIds: String)

    suspend fun updateAnswerContent(answerId: String, content: String)

    suspend fun updateAnswerCorrectness(answerId: String, isCorrect: Boolean)
}