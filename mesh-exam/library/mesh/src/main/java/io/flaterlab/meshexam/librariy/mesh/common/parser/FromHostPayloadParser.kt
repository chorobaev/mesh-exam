package io.flaterlab.meshexam.librariy.mesh.common.parser

import com.google.gson.Gson
import io.flaterlab.meshexam.librariy.mesh.common.dto.FromHostPayload

internal class FromHostPayloadParser(
    private val gson: Gson
) : JsonParser<FromHostPayload> {

    override fun fromJson(json: String): FromHostPayload {
        return gson.fromJson(json, FromHostPayload::class.java)
    }

    override fun toJson(model: FromHostPayload): String {
        return gson.toJson(model)
    }
}