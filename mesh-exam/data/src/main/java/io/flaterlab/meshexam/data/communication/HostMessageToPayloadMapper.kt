package io.flaterlab.meshexam.data.communication

import com.google.gson.Gson
import io.flaterlab.meshexam.core.Mapper
import io.flaterlab.meshexam.librariy.mesh.common.dto.FromHostPayload
import timber.log.Timber
import javax.inject.Inject

internal class HostMessageToPayloadMapper @Inject constructor(
    private val gson: Gson,
) : Mapper<HostMessage, FromHostPayload> {

    override fun invoke(input: HostMessage): FromHostPayload {
        return FromHostPayload(
            contentType = input.companionObject.contentType,
            data = gson.toJson(input).also { Timber.d("Host message json being send: $it") }
        )
    }
}

interface HostMessage {

    val companionObject: MeshMessageCompanion

    val clientId: String? get() = null
}

interface MeshMessageCompanion {

    val contentType: String
}