package io.flaterlab.meshexam.data.mapper

import io.flaterlab.meshexam.core.Mapper
import io.flaterlab.meshexam.data.database.entity.QuestionEntity
import io.flaterlab.meshexam.domain.create.model.CreateQuestionModel
import java.util.*
import javax.inject.Inject

internal class CreateQuestionModelToEntityMapper @Inject constructor(

) : Mapper<CreateQuestionModel, QuestionEntity> {

    override fun invoke(input: CreateQuestionModel): QuestionEntity {
        return QuestionEntity(
            questionId = UUID.randomUUID().toString(),
            hostExamId = input.examId,
            question = "",
            type = input.type.toString(),
            score = input.score,
            orderNumber = input.orderNumber,
            createdAt = Date().time,
            updatedAt = Date().time,
        )
    }
}