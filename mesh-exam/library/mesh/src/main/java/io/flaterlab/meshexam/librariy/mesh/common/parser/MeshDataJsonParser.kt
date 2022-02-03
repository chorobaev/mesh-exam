package io.flaterlab.meshexam.librariy.mesh.common.parser

import com.google.gson.Gson
import io.flaterlab.meshexam.librariy.mesh.common.dto.MeshData

internal class ClientConnectedJsonParser(
    private val gson: Gson,
) : JsonParser<MeshData.ClientConnected> {

    override fun fromJson(json: String): MeshData.ClientConnected {
        return gson.fromJson(json, MeshData.ClientConnected::class.java)
    }

    override fun toJson(model: MeshData.ClientConnected): String {
        return gson.toJson(model)
    }
}