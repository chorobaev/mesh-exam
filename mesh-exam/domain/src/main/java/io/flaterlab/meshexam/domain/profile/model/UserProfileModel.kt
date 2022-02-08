package io.flaterlab.meshexam.domain.profile.model

data class UserProfileModel(
    val id: String,
    val firstName: String,
    val lastName: String,
    val info: String,
    val initials: String,
)