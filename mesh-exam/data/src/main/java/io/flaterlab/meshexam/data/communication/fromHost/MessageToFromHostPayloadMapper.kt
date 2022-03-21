package io.flaterlab.meshexam.data.communication.fromHost

import com.google.gson.Gson
import io.flaterlab.meshexam.core.Mapper
import io.flaterlab.meshexam.data.communication.Message
import io.flaterlab.meshexam.librariy.mesh.common.dto.FromHostPayload
import timber.log.Timber
import javax.inject.Inject

internal class MessageToFromHostPayloadMapper @Inject constructor(
    private val gson: Gson,
) : Mapper<Message, FromHostPayload> {

    override fun invoke(input: Message): FromHostPayload {
        return FromHostPayload(
            contentType = input.companionObject.contentType,
            data = gson.toJson(input).also { Timber.d("Host message json being send: $it") }
        )
    }
}