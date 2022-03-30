package io.flaterlab.meshexam.domain.profile.model

data class HostingResultItemModel(
    val id: String,
    val studentFullName: String,
    val studentInfo: String,
    val status: Status,
    val grade: Float,
    val totalGrade: Int,
) {
    enum class Status {
        SUBMITTED,
        NOT_SUBMITTED
        ;
    }
}