package io.flaterlab.meshexam.domain.interactor

import io.flaterlab.meshexam.domain.profile.model.*
import kotlinx.coroutines.flow.Flow

interface ProfileInteractor {

    suspend fun isProfileSetUp(): Boolean

    fun userProfile(): Flow<UserProfileModel>

    fun examHistory(): Flow<List<ExamHistoryModel>>

    fun hostingResultMeta(hostingId: String): Flow<HostingResultMetaModel>

    fun hostingResults(hostingId: String): Flow<List<HostingResultItemModel>>

    fun individualResultMeta(attemptId: String): Flow<IndividualResultModel>

    fun questionResult(questionId: String, attemptId: String): Flow<QuestionResultModel>
}