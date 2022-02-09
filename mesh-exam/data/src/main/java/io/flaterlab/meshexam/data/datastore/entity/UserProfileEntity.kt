package io.flaterlab.meshexam.data.datastore.entity

import com.google.gson.annotations.Expose

internal data class UserProfileEntity(
    @Expose val id: String,
    @Expose val firstName: String?,
    @Expose val lastName: String?,
    @Expose val info: String?,
) {

    val fullName get() = "$firstName $lastName"
}