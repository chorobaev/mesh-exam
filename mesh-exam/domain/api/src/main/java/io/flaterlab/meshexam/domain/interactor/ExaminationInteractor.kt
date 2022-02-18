package io.flaterlab.meshexam.domain.interactor

import io.flaterlab.meshexam.domain.create.model.ExamInfoModel
import io.flaterlab.meshexam.domain.exam.model.ExamStateModel
import kotlinx.coroutines.flow.Flow

interface ExaminationInteractor {

    fun discoverExams(): Flow<List<ExamInfoModel>>

    suspend fun joinExam(examId: String)

    fun examState(examId: String): Flow<ExamStateModel>

    suspend fun  leaveExam(examId: String)
}