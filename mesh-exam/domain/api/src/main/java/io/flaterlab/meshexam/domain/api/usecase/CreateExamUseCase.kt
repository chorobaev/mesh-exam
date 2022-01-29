package io.flaterlab.meshexam.domain.api.usecase

import io.flaterlab.meshexam.domain.api.model.CreateExamModel

interface CreateExamUseCase {

    suspend operator fun invoke(modelCreate: CreateExamModel): String
}