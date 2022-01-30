package io.flaterlab.meshexam.domain.api.datasource

import io.flaterlab.meshexam.domain.api.model.CreateExamModel
import io.flaterlab.meshexam.domain.api.model.ExamModel
import io.flaterlab.meshexam.domain.api.model.ExamWithQuestionIdsModel
import kotlinx.coroutines.flow.Flow

interface ExamDataSource {

    suspend fun createTest(createExam: CreateExamModel): String

    fun exams(): Flow<List<ExamModel>>

    suspend fun getExamWithQuestionIdsByExamId(examId: String): ExamWithQuestionIdsModel
}