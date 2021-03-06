package io.flaterlab.meshexam.librariy.mesh.common.parser

import com.google.gson.Gson
import io.flaterlab.meshexam.librariy.mesh.common.dto.MeshPayload
import javax.inject.Inject

internal class MeshPayloadJsonParser @Inject constructor(
    private val gson: Gson,
) : JsonParser<MeshPayload> {

    override fun fromJson(json: String): MeshPayload {
        return gson.fromJson(json, MeshPayload::class.java)
    }

    override fun toJson(model: MeshPayload): String {
        return gson.toJson(model)
    }
}