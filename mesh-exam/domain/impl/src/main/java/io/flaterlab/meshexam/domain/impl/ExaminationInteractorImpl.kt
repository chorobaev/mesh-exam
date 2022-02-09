package io.flaterlab.meshexam.domain.impl

import io.flaterlab.meshexam.domain.api.model.ExamInfoModel
import io.flaterlab.meshexam.domain.interactor.ExaminationInteractor
import io.flaterlab.meshexam.domain.repository.DiscoveryRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class ExaminationInteractorImpl @Inject constructor(
    private val discoveryRepository: DiscoveryRepository,
) : ExaminationInteractor {

    override fun discoverExams(): Flow<List<ExamInfoModel>> {
        return discoveryRepository.discoverExams()
    }

    override suspend fun joinExam(examId: String) {
        discoveryRepository.joinExam(examId)
    }
}