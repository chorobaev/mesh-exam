package io.flaterlab.meshexam.domain.mesh.model

data class HostedStudentModel(
    val userId: String,
    val fullName: String,
    val info: String,
    val status: Status,
) {
    enum class Status {
        ATTEMPTING,
        DISCONNECTED,
        SUBMITTED,
        ;
    }
}