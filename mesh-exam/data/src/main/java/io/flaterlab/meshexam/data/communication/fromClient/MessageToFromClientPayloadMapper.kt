package io.flaterlab.meshexam.data.communication.fromClient

import com.google.gson.Gson
import io.flaterlab.meshexam.core.Mapper
import io.flaterlab.meshexam.data.communication.MeshMessage
import io.flaterlab.meshexam.librariy.mesh.common.dto.FromClientPayload
import timber.log.Timber
import javax.inject.Inject

internal class MessageToFromClientPayloadMapper @Inject constructor(
    private val gson: Gson,
) : Mapper<MeshMessage, FromClientPayload> {

    override fun invoke(input: MeshMessage): FromClientPayload {
        return FromClientPayload(
            contentType = input.companionObject.contentType,
            data = gson.toJson(input).also { Timber.d("Attempt to be send: $it") }
        )
    }
}