package io.flaterlab.meshexam.presentation.profile.dvo

internal data class UserProfileDvo(
    val firstName: String,
    val lastName: String,
    val initials: String,
    val info: String,
) {

    val fullName get() = "$firstName $lastName"
}