package io.flaterlab.meshexam.domain.impl

import io.flaterlab.meshexam.domain.interactor.ProfileInteractor
import io.flaterlab.meshexam.domain.profile.model.UserProfileModel
import io.flaterlab.meshexam.domain.repository.ProfileRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.firstOrNull
import javax.inject.Inject

class ProfileInteractorImpl @Inject constructor(
    private val profileRepository: ProfileRepository,
) : ProfileInteractor {

    override suspend fun isProfileSetUp(): Boolean {
        val userProfile = profileRepository.userProfile().firstOrNull()
        return !userProfile?.fullName.isNullOrBlank()
    }

    override fun userProfile(): Flow<UserProfileModel> = profileRepository.userProfile()
}