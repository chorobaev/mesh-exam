package io.flaterlab.meshexam.domain.results

data class SendingRequestModel(
    val uid: String,
    val studentFullName: String,
    val examName: String,
    val startTimeInMillis: Long,
)