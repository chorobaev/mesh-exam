package io.flaterlab.meshexam.librariy.mesh.common.parser

import com.google.gson.Gson
import io.flaterlab.meshexam.librariy.mesh.common.dto.*

internal class JsonParserHelper(
    val advertiserInfoJsonParser: JsonParser<AdvertiserInfo>,
    val clientInfoJsonParser: JsonParser<ClientInfo>,
    val meshPayloadJsonParser: JsonParser<MeshPayload>,
    val clientConnectedJsonParser: JsonParser<MeshData.ClientConnected>,
    val clientDisconnectedJsonParser: JsonParser<MeshData.ClientDisconnected>,
    val fromClientPayloadParser: JsonParser<FromClientPayload>,
) {

    companion object {
        fun getInstance(gson: Gson) = JsonParserHelper(
            AdvertiserInfoJsonParser(gson),
            ClientInfoJsonParser(gson),
            MeshPayloadJsonParser(gson),
            ClientConnectedJsonParser(gson),
            ClientDisconnectedJsonParser(gson),
            FromClientPayloadParser(gson),
        )
    }
}