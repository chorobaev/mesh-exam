package io.flaterlab.meshexam.domain.exam.model

data class AttemptMetaModel(
    val examId: String,
    val attemptId: String,
    val examName: String,
    val examInfo: String,
    val examStatus: ExamStatus,
    val leftTimeInMillis: Long,
) {

    enum class ExamStatus {
        STARTED,
        FINISHED,
        ;
    }
}