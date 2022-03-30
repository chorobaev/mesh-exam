package io.flaterlab.meshexam.domain.interactor

import io.flaterlab.meshexam.domain.profile.model.ExamHistoryModel
import io.flaterlab.meshexam.domain.profile.model.HostingResultMetaModel
import io.flaterlab.meshexam.domain.profile.model.HostingResultItemModel
import io.flaterlab.meshexam.domain.profile.model.UserProfileModel
import kotlinx.coroutines.flow.Flow

interface ProfileInteractor {

    suspend fun isProfileSetUp(): Boolean

    fun userProfile(): Flow<UserProfileModel>

    fun examHistory(): Flow<List<ExamHistoryModel>>

    fun hostingResultMeta(hostingId: String): Flow<HostingResultMetaModel>

    fun hostingResults(hostingId: String): Flow<List<HostingResultItemModel>>
}