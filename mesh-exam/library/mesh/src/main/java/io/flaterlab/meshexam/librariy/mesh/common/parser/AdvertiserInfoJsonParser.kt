package io.flaterlab.meshexam.librariy.mesh.common.parser

import com.google.gson.Gson
import io.flaterlab.meshexam.librariy.mesh.host.AdvertiserInfo

internal class AdvertiserInfoJsonParser(
    private val gson: Gson,
) : JsonParser<AdvertiserInfo> {

    override fun toJson(model: AdvertiserInfo): String {
        return gson.toJson(model)
    }

    override fun fromJson(json: String): AdvertiserInfo {
        return gson.fromJson(json, AdvertiserInfo::class.java)
    }
}