package io.flaterlab.meshexam.domain.profile.model

data class UpdateUserProfileModel(
    val firstName: String,
    val lastName: String,
    val info: String
)