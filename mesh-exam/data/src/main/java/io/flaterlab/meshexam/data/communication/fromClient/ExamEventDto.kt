package io.flaterlab.meshexam.data.communication.fromClient

import com.google.gson.annotations.Expose
import io.flaterlab.meshexam.data.communication.MeshMessageCompanion
import io.flaterlab.meshexam.data.communication.MeshMessage

internal data class ExamEventDto(
    @Expose override val clientId: String,
    @Expose val hostingId: String,
    @Expose val eventType: EventType,
) : MeshMessage {

    override val companionObject: MeshMessageCompanion = Companion

    @JvmInline
    value class EventType private constructor(
        val typeInt: Int
    ) {
        companion object {
            val SCREEN_HID get() = EventType(1)
            val SCREEN_VISIBLE get() = EventType(2)

            @JvmStatic
            fun fromInt(value: Int) = EventType(value)
        }
    }

    companion object : MeshMessageCompanion {

        override val contentType: String = "EXAM_EVENT"
    }
}