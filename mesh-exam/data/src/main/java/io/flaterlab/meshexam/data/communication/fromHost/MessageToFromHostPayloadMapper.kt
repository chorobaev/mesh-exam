package io.flaterlab.meshexam.data.communication.fromHost

import com.google.gson.Gson
import io.flaterlab.meshexam.core.Mapper
import io.flaterlab.meshexam.data.communication.MeshMessage
import io.flaterlab.meshexam.librariy.mesh.common.dto.FromHostPayload
import timber.log.Timber
import javax.inject.Inject

internal class MessageToFromHostPayloadMapper @Inject constructor(
    private val gson: Gson,
) : Mapper<MeshMessage, FromHostPayload> {

    override fun invoke(input: MeshMessage): FromHostPayload {
        return FromHostPayload(
            contentType = input.companionObject.contentType,
            data = gson.toJson(input).also { Timber.d("Host message json being send: $it") }
        )
    }
}