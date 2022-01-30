package io.flaterlab.meshexam.domain.datasource

import io.flaterlab.meshexam.domain.api.model.CreateExamModel
import io.flaterlab.meshexam.domain.api.model.ExamModel
import io.flaterlab.meshexam.domain.create.model.*
import kotlinx.coroutines.flow.Flow

interface ExamDataSource {

    fun exams(): Flow<List<ExamModel>>

    suspend fun createExam(createExam: CreateExamModel): String

    suspend fun getExamWithQuestionIdsByExamId(examId: String): ExamWithQuestionIdsModel


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