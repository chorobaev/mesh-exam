package io.flaterlab.meshexam.domain.create.model

import io.flaterlab.meshexam.domain.api.model.ExamModel

data class ExamWithQuestionIdsModel(
    val exam: ExamModel,
    val questionIds: List<String>,
)