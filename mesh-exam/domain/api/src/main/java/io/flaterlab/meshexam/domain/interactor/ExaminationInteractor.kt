package io.flaterlab.meshexam.domain.interactor

interface ExaminationInteractor {

    suspend fun joinExam(examId: String)
}