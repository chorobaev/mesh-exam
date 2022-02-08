package io.flaterlab.meshexam.domain.profile.usecase

import io.flaterlab.meshexam.domain.profile.model.UpdateUserProfileModel
import io.flaterlab.meshexam.domain.repository.ProfileRepository
import javax.inject.Inject

class UpdateUserProfileUseCase @Inject constructor(
    private val profileRepository: ProfileRepository,
) {

    suspend operator fun invoke(model: UpdateUserProfileModel) {
        profileRepository.updateUserProfile(model)
    }
}