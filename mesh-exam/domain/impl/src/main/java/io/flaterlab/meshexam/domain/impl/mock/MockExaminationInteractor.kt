package io.flaterlab.meshexam.domain.impl.mock

import io.flaterlab.meshexam.domain.api.model.ExamInfoModel
import io.flaterlab.meshexam.domain.interactor.ExaminationInteractor
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class MockExaminationInteractor @Inject constructor(

) : ExaminationInteractor {

    override fun discoverExams(): Flow<List<ExamInfoModel>> {
        return flowOf((1..5))
            .map { range ->
                range.map {
                    ExamInfoModel(it.toString(), "Exam #$it", "Mock User #$it", 20)
                }
            }
    }

    override suspend fun joinExam(examId: String) {

    }
}