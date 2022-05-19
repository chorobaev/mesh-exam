package io.flaterlab.meshexam.data.communication

interface MeshMessage {

    val companionObject: MeshMessageCompanion

    val clientId: String? get() = null
}

interface MeshMessageCompanion {

    val contentType: String
}