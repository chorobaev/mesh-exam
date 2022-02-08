package io.flaterlab.meshexam.data.mapper

import io.flaterlab.meshexam.core.Mapper
import io.flaterlab.meshexam.data.datastore.entity.UserProfileEntity
import io.flaterlab.meshexam.domain.profile.model.UserProfileModel
import javax.inject.Inject

internal class UserProfileEntityToModelMapper @Inject constructor(

) : Mapper<UserProfileEntity, UserProfileModel> {

    override fun invoke(input: UserProfileEntity): UserProfileModel {
        return UserProfileModel(
            input.id,
            input.firstName.orEmpty(),
            input.lastName.orEmpty(),
            input.info.orEmpty(),
            provideInitials(input),
        )
    }

    private fun provideInitials(input: UserProfileEntity): String {
        val first = input.firstName ?: return ""
        val last = input.lastName ?: return ""
        return if (first.isBlank() || last.isBlank()) {
            ""
        } else {
            "${first.first()}${last.first()}".uppercase()
        }
    }
}