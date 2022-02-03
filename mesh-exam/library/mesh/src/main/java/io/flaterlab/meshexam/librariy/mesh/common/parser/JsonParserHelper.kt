package io.flaterlab.meshexam.librariy.mesh.common.parser

import com.google.gson.Gson
import io.flaterlab.meshexam.librariy.mesh.common.dto.AdvertiserInfo
import io.flaterlab.meshexam.librariy.mesh.common.dto.ClientInfo
import io.flaterlab.meshexam.librariy.mesh.common.dto.MeshData
import io.flaterlab.meshexam.librariy.mesh.common.dto.MeshPayload

internal class JsonParserHelper(
    val advertiserInfoJsonParser: JsonParser<AdvertiserInfo>,
    val clientInfoJsonParser: JsonParser<ClientInfo>,
    val meshPayloadJsonParser: JsonParser<MeshPayload>,
    val clientConnectedJsonParser: JsonParser<MeshData.ClientConnected>
) {

    companion object {
        fun getInstance(gson: Gson) = JsonParserHelper(
            AdvertiserInfoJsonParser(gson),
            ClientInfoJsonParser(gson),
            MeshPayloadJsonParser(gson),
            ClientConnectedJsonParser(gson),
        )
    }
}