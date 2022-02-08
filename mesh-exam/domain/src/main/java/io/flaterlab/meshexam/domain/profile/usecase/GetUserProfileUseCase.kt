package io.flaterlab.meshexam.domain.profile.usecase

import io.flaterlab.meshexam.domain.repository.ProfileRepository
import io.flaterlab.meshexam.domain.profile.model.UserProfileModel
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetUserProfileUseCase @Inject constructor(
    private val profileRepository: ProfileRepository,
) {

    operator fun invoke(): Flow<UserProfileModel> = profileRepository.userProfile()
}