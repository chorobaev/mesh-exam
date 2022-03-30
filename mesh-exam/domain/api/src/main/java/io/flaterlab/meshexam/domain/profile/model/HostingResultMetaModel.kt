package io.flaterlab.meshexam.domain.profile.model

data class HostingResultMetaModel(
    val hostingId: String,
    val examName: String,
    val examInfo: String,
    val durationInMillis: Long,
    val submissionCount: Int,
    val expectedSubmissionCount: Int,
)