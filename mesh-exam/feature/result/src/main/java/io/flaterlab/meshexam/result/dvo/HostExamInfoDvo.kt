package io.flaterlab.meshexam.result.dvo

internal data class HostExamInfoDvo(
    val examName: String,
    val info: String,
    val duration: String,
    val submissionCount: Int,
    val expectedSubmissionCount: Int,
)