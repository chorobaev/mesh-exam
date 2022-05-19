package io.flaterlab.meshexam.data.communication.fromClient

import com.google.gson.annotations.Expose
import io.flaterlab.meshexam.data.communication.MeshMessageCompanion
import io.flaterlab.meshexam.data.communication.MeshMessage

internal data class AttemptDto(
    @Expose val id: String,
    @Expose val userId: String,
    @Expose val examId: String,
    @Expose val hostingId: String,
    @Expose val startedAt: Long,
    @Expose val finishedAt: Long,
    @Expose val answers: List<AttemptAnswerDto>,
) : MeshMessage {

    override val companionObject: MeshMessageCompanion = Companion

    companion object : MeshMessageCompanion {
        override val contentType: String = "ATTEMPT_RESULT"
    }
}

internal data class AttemptAnswerDto(
    @Expose val id: String,
    @Expose val questionId: String,
    @Expose val answerId: String,
    @Expose val createdAt: Long,
)