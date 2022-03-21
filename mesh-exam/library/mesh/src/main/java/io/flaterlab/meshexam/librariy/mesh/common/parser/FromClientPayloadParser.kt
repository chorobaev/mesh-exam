package io.flaterlab.meshexam.librariy.mesh.common.parser

import com.google.gson.Gson
import io.flaterlab.meshexam.librariy.mesh.common.dto.FromClientPayload
import javax.inject.Inject

internal class FromClientPayloadParser @Inject constructor(
    private val gson: Gson,
): JsonParser<FromClientPayload> {

    override fun fromJson(json: String): FromClientPayload {
        return gson.fromJson(json, FromClientPayload::class.java)
    }

    override fun toJson(model: FromClientPayload): String {
        return gson.toJson(model)
    }
}