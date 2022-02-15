package io.flaterlab.meshexam.domain.mesh.model

data class ClientModel(
    val id: String,
    val fullName: String,
    val info: String,
    val positionInMesh: Int,
)