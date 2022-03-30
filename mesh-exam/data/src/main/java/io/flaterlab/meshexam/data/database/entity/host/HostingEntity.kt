package io.flaterlab.meshexam.data.database.entity.host

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "hostings")
internal data class HostingEntity(
    @PrimaryKey val hostingId: String,
    val userId: String,
    val examId: String,
    val startedAt: Long,
    val finishedAt: Long?,
) {

    val durationInMillis: Long
        get() = (finishedAt
            ?: throw IllegalStateException("Hosting must be finished. finishedAt == null")) -
                startedAt
}