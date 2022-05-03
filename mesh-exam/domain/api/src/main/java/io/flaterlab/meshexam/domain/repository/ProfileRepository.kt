package io.flaterlab.meshexam.domain.repository

import io.flaterlab.meshexam.domain.profile.model.AppInfoModel
import io.flaterlab.meshexam.domain.profile.model.UpdateUserProfileModel
import io.flaterlab.meshexam.domain.profile.model.UserProfileModel
import kotlinx.coroutines.flow.Flow

interface ProfileRepository {

    fun userProfile(): Flow<UserProfileModel>

    suspend fun updateUserProfile(profile: UpdateUserProfileModel)

    suspend fun getAppInfo(): AppInfoModel
}