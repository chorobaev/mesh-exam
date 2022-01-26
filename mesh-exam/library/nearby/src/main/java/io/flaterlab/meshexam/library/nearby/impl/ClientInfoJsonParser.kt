package io.flaterlab.meshexam.library.nearby.impl

import com.google.gson.Gson
import javax.inject.Inject

internal class ClientInfoJsonParser @Inject constructor(
    private val gson: Gson,
) : NearbyFacadeImpl.JsonParser<ClientInfo> {

    override fun fromJson(json: String): ClientInfo {
        return gson.fromJson(json, ClientInfo::class.java)
    }

    override fun toJson(model: ClientInfo): String {
        return gson.toJson(model)
    }
}