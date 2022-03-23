package io.flaterlab.meshexam.data.database.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "users")
internal data class UserEntity(
    @PrimaryKey val userId: String,
    val fullName: String,
    val info: String,
)