package io.flaterlab.meshexam.data.mapper

import io.flaterlab.meshexam.core.Mapper
import io.flaterlab.meshexam.data.database.entity.ExamEntity
import io.flaterlab.meshexam.domain.create.model.ExamModel
import javax.inject.Inject

internal class ExamEntityToModelMapper @Inject constructor() : Mapper<ExamEntity, ExamModel>{

    override fun invoke(input: ExamEntity): ExamModel {
        return ExamModel(input.examId, input.name, input.type, input.durationInMin)
    }
}