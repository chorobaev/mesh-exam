package io.flaterlab.meshexam.domain.interactor

import io.flaterlab.meshexam.domain.create.model.ExamModel

interface ExamContentInteractor {

    suspend fun deleteExamById(examId: String)

    suspend fun updateExam(model: ExamModel)
}