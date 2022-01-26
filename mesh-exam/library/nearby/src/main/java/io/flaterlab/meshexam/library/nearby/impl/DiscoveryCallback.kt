package io.flaterlab.meshexam.library.nearby.impl

import com.google.android.gms.nearby.connection.DiscoveredEndpointInfo
import com.google.android.gms.nearby.connection.EndpointDiscoveryCallback
import timber.log.Timber
import java.util.*

internal class DiscoveryCallback<T>(
    private val jsonParser: (String) -> T,
    private val onChangeListener: (Map<String, T>) -> Unit = {},
) : EndpointDiscoveryCallback() {

    val advertiserMap: MutableMap<String, T> = TreeMap()

    override fun onEndpointFound(endpointId: String, info: DiscoveredEndpointInfo) {
        try {
            val json = String(info.endpointInfo)
            val advertiserInfo = jsonParser(json)
            advertiserMap[endpointId] = advertiserInfo
            onChangeListener(advertiserMap)
        } catch (ignored: Exception) {
            Timber.e(ignored)
        }
    }

    override fun onEndpointLost(endpointId: String) {
        advertiserMap.remove(endpointId)
        onChangeListener(advertiserMap)
    }
}