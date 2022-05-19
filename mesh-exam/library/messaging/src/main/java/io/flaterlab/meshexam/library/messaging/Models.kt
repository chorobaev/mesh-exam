package io.flaterlab.meshexam.library.messaging

sealed class Message {

    data class Payload(
        val senderInfo: SenderInfo,
        val receiverId: String,
        val content: String,
    ) : Message()

    data class Request(
        val uid: String,
        val endpointId: String,
    ) : Message()
}

data class SenderInfo(
    val uid: String,
)

data class ReceiverInfo(
    val uid: String,
)