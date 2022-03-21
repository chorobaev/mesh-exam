package io.flaterlab.meshexam.data.communication

interface Message {

    val companionObject: MeshMessageCompanion

    val clientId: String? get() = null
}

interface MeshMessageCompanion {

    val contentType: String
}