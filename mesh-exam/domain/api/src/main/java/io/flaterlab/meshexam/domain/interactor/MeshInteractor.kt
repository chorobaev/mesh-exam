package io.flaterlab.meshexam.domain.interactor

interface MeshInteractor {

    fun stopMesh()

    suspend fun startExam(examId: String)
}