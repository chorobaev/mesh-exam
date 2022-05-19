package io.flaterlab.meshexam.data.communication.fromHost

import com.google.gson.annotations.Expose
import io.flaterlab.meshexam.data.communication.MeshMessageCompanion
import io.flaterlab.meshexam.data.communication.MeshMessage

internal data class FinishExamEventDto(
    @Expose val hostingId: String,
) : MeshMessage {

    override val companionObject: MeshMessageCompanion = Companion

    companion object : MeshMessageCompanion {

        override val contentType: String = "FINISH_EXAM_EVENT"
    }
}