package io.flaterlab.meshexam.data.mapper

import io.flaterlab.meshexam.core.Mapper
import io.flaterlab.meshexam.data.database.entity.ExamEntity
import io.flaterlab.meshexam.domain.api.model.CreateExamModel
import java.util.*
import javax.inject.Inject

internal class CreateExamModelToEntityMapper @Inject constructor() : Mapper<CreateExamModel, ExamEntity> {

    override fun invoke(input: CreateExamModel): ExamEntity {
        return ExamEntity(
            examId = UUID.randomUUID().toString(),
            name = input.name,
            type = input.type ?: "",
            durationInMin = input.durationInMin,
        )
    }
}