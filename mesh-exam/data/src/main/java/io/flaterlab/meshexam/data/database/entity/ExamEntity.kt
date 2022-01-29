package io.flaterlab.meshexam.data.database.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "exams")
internal data class ExamEntity(
    @PrimaryKey val examId: String,
    val name: String,
    val type: String,
    val durationInMin: Long,
)