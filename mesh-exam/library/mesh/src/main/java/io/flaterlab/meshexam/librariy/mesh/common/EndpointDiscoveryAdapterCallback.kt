package io.flaterlab.meshexam.librariy.mesh.common

import com.google.android.gms.nearby.connection.DiscoveredEndpointInfo
import com.google.android.gms.nearby.connection.EndpointDiscoveryCallback
import com.google.gson.Gson
import com.google.gson.JsonSyntaxException
import io.flaterlab.meshexam.librariy.mesh.common.dto.AdvertiserInfo
import timber.log.Timber

internal class EndpointDiscoveryAdapterCallback(
    private val gson: Gson,
) : EndpointDiscoveryCallback() {

    var adapterCallback: AdapterCallback? = null

    override fun onEndpointFound(endpointId: String, info: DiscoveredEndpointInfo) {
        try {
            val json = String(info.endpointInfo)
            val advertiserInfo = gson.fromJson(json, AdvertiserInfo::class.java)
            adapterCallback?.onAdvertiserFound(endpointId, advertiserInfo)
        } catch (ignored: JsonSyntaxException) {
            Timber.e(ignored)
        }
    }

    override fun onEndpointLost(endpointId: String) {
        adapterCallback?.onAdvertiserLost(endpointId)
    }

    interface AdapterCallback {

        fun onAdvertiserFound(endpointId: String, advertiserInfo: AdvertiserInfo)

        fun onAdvertiserLost(endpointId: String)
    }
}