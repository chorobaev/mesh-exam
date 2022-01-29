package io.flaterlab.meshexam.domain.api.datasource

import io.flaterlab.meshexam.domain.api.model.CreateExamModel
import io.flaterlab.meshexam.domain.api.model.ExamModel
import kotlinx.coroutines.flow.Flow

interface ExamDataSource {

    suspend fun createTest(createExam: CreateExamModel): String

    fun exams(): Flow<List<ExamModel>>
}