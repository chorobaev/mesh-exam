package io.flaterlab.meshexam.data.communication.fromHost

import com.google.gson.annotations.Expose
import io.flaterlab.meshexam.data.communication.MeshMessageCompanion
import io.flaterlab.meshexam.data.communication.Message

internal data class FinishExamEventDto(
    @Expose val hostingId: String,
) : Message {

    override val companionObject: MeshMessageCompanion = Companion

    companion object : MeshMessageCompanion {

        override val contentType: String = "FINISH_EXAM_EVENT"
    }
}