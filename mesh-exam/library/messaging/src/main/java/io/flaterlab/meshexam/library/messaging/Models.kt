package io.flaterlab.meshexam.library.messaging

data class Message(
    val senderInfo: SenderInfo,
    val receiverId: String,
    val content: String,
)

data class SenderInfo(
    val uid: String,
)

data class ReceiverInfo(
    val uid: String,
)