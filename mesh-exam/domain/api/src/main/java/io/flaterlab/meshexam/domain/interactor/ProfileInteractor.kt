package io.flaterlab.meshexam.domain.interactor

import io.flaterlab.meshexam.domain.profile.model.UserProfileModel
import kotlinx.coroutines.flow.Flow

interface ProfileInteractor {

    suspend fun isProfileSetUp(): Boolean

    fun userProfile(): Flow<UserProfileModel>
}