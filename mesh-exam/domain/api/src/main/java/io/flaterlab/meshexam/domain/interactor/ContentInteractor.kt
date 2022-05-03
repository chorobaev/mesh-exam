package io.flaterlab.meshexam.domain.interactor

interface ContentInteractor {

    suspend fun deleteExamById(examId: String)
}