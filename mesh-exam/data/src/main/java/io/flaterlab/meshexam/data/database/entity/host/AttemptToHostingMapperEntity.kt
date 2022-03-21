package io.flaterlab.meshexam.data.database.entity.host

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "attempt_to_hosting_mapper")
internal data class AttemptToHostingMapperEntity(
    @PrimaryKey val attemptId: String,
    val hostingId: String,
)