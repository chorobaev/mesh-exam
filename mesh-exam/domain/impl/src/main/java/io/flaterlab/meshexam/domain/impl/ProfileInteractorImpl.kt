package io.flaterlab.meshexam.domain.impl

import io.flaterlab.meshexam.domain.interactor.ProfileInteractor
import io.flaterlab.meshexam.domain.profile.model.ExamHistoryModel
import io.flaterlab.meshexam.domain.profile.model.HostingResultItemModel
import io.flaterlab.meshexam.domain.profile.model.HostingResultMetaModel
import io.flaterlab.meshexam.domain.profile.model.UserProfileModel
import io.flaterlab.meshexam.domain.repository.HistoryRepository
import io.flaterlab.meshexam.domain.repository.ProfileRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.firstOrNull
import javax.inject.Inject

class ProfileInteractorImpl @Inject constructor(
    private val profileRepository: ProfileRepository,
    private val historyRepository: HistoryRepository,
) : ProfileInteractor {

    override suspend fun isProfileSetUp(): Boolean {
        val userProfile = profileRepository.userProfile().firstOrNull()
        return !userProfile?.fullName.isNullOrBlank()
    }

    override fun userProfile(): Flow<UserProfileModel> = profileRepository.userProfile()

    override fun examHistory(): Flow<List<ExamHistoryModel>> {
        return historyRepository.userExamHistory()
    }

    override fun hostingResultMeta(hostingId: String): Flow<HostingResultMetaModel> {
        return historyRepository.hostingResultMeta(hostingId)
    }

    override fun hostingResults(hostingId: String): Flow<List<HostingResultItemModel>> {
        return historyRepository.hostingResults(hostingId)
    }
}