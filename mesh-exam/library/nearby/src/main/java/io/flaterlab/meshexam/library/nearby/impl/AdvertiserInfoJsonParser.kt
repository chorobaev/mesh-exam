package io.flaterlab.meshexam.library.nearby.impl

import com.google.gson.Gson
import javax.inject.Inject

internal class AdvertiserInfoJsonParser @Inject constructor(
    private val gson: Gson,
) : NearbyFacadeImpl.JsonParser<AdvertiserInfo> {

    override fun fromJson(json: String): AdvertiserInfo {
        return gson.fromJson(json, AdvertiserInfo::class.java)
    }

    override fun toJson(model: AdvertiserInfo): String {
        return gson.toJson(model)
    }
}