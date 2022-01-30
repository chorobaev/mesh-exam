package io.flaterlab.meshexam.domain.model

import io.flaterlab.meshexam.domain.api.model.ExamModel

data class ExamWithQuestionIdsModel(
    val exam: ExamModel,
    val questionIds: List<String>,
)