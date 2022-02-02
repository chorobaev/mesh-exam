package io.flaterlab.meshexam.librariy.mesh.common.parser

import com.google.gson.Gson
import io.flaterlab.meshexam.librariy.mesh.common.dto.ClientInfo

internal class ClientInfoJsonParser(
    private val gson: Gson,
) : JsonParser<ClientInfo> {

    override fun toJson(model: ClientInfo): String {
        return gson.toJson(model)
    }

    override fun fromJson(json: String): ClientInfo {
        return gson.fromJson(json, ClientInfo::class.java)
    }
}