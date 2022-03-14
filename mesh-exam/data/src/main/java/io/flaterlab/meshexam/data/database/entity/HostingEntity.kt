package io.flaterlab.meshexam.data.database.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "hostings")
internal data class HostingEntity(
    @PrimaryKey val hostingId: String,
    val userId: String,
    val examId: String,
)