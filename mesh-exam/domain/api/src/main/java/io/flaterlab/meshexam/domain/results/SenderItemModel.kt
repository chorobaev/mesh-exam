package io.flaterlab.meshexam.domain.results

data class SenderItemModel(
    val id: String,
    val studentFullName: String,
    val studentInfo: String,
    val status: Status,
    val grade: Float,
    val totalGrade: Int,
) {
    enum class Status {
        IN_PROGRESS,
        ERROR,
        RECEIVED
        ;
    }
}