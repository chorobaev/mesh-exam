package io.flaterlab.meshexam.data.database.entity.client

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "exam_to_hosting_mapper")
internal data class ExamToHostingMapperEntity(
    @PrimaryKey val examId: String,
    val hostingId: String,
)