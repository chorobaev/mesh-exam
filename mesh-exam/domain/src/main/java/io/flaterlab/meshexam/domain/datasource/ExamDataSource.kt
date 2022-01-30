package io.flaterlab.meshexam.domain.datasource

import io.flaterlab.meshexam.domain.api.model.CreateExamModel
import io.flaterlab.meshexam.domain.api.model.ExamModel
import io.flaterlab.meshexam.domain.model.ExamWithQuestionIdsModel
import io.flaterlab.meshexam.domain.model.CreateQuestionModel
import kotlinx.coroutines.flow.Flow

interface ExamDataSource {

    suspend fun createExam(createExam: CreateExamModel): String

    fun exams(): Flow<List<ExamModel>>

    suspend fun getExamWithQuestionIdsByExamId(examId: String): ExamWithQuestionIdsModel

    suspend fun createQuestion(model: CreateQuestionModel): String

    suspend fun deleteQuestions(vararg questionIds: String)
}