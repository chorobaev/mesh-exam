package io.flaterlab.meshexam.domain.profile.model

data class HostingResultModel(
    val id: String,
    val studentFullName: String,
    val studentInfo: String,
    val status: Status,
    val grade: Int,
    val totalGrade: Int,
) {
    enum class Status {
        SUBMITTED,
        NOT_SUBMITTED
        ;
    }
}