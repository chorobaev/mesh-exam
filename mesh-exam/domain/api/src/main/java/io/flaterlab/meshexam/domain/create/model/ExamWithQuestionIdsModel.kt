package io.flaterlab.meshexam.domain.create.model

data class ExamWithQuestionIdsModel(
    val exam: ExamModel,
    val questionIds: List<String>,
)