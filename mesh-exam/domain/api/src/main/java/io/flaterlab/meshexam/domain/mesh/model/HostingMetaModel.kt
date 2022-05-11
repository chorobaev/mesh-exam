package io.flaterlab.meshexam.domain.mesh.model

data class HostingMetaModel(
    val status: HostingStatus,
) {

    enum class HostingStatus {
        STARTED,
        FINISHED,
        ;
    }
}