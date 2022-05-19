package io.flaterlab.meshexam.data.communication.fromHost

import com.google.gson.annotations.Expose
import io.flaterlab.meshexam.data.communication.MeshMessageCompanion
import io.flaterlab.meshexam.data.communication.MeshMessage

internal data class ExamDto(
    @Expose val id: String,
    @Expose val hostUserId: String,
    @Expose val hostingId: String,
    @Expose val durationInMin: Int,
    @Expose val name: String,
    @Expose val questions: List<QuestionDto>,
    @Expose val type: String,
) : MeshMessage {

    override val companionObject: MeshMessageCompanion get() = Companion

    companion object : MeshMessageCompanion {

        override val contentType: String = "EXAM_CONTENT"
    }
}

internal data class QuestionDto(
    @Expose val id: String,
    @Expose val answers: List<AnswerDto>,
    @Expose val orderNumber: Int,
    @Expose val question: String,
    @Expose val score: Float,
    @Expose val type: String,
)

internal data class AnswerDto(
    @Expose val id: String,
    @Expose val answer: String,
    @Expose val isCorrect: Boolean,
    @Expose val orderNumber: Int,
)