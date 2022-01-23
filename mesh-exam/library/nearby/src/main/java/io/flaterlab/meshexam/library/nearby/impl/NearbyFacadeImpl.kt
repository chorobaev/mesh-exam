package io.flaterlab.meshexam.library.nearby.impl

import com.google.android.gms.nearby.connection.*
import com.google.gson.Gson
import io.flaterlab.meshexam.library.nearby.api.NearbyFacade
import io.flaterlab.meshexam.library.nearby.impl.dto.AdvertiserInfo
import kotlinx.coroutines.tasks.await

internal class NearbyFacadeImpl(
    private val serviceId: String,
    private val client: ConnectionsClient,
    private val gson: Gson,
) : NearbyFacade {

    private val strategy: Strategy = Strategy.P2P_CLUSTER
    private val advertisingOptions = AdvertisingOptions.Builder()
        .setStrategy(strategy)
        .setDisruptiveUpgrade(false)
        .build()
    private val discoveryOperations = DiscoveryOptions.Builder()
        .setStrategy(strategy)
        .build()

    private val connectionCallback: ConnectionLifecycleCallback = ConnectionCallback()
    private val discoveryCallback: EndpointDiscoveryCallback = DiscoveryCallback()

    private var isAdvertising = false
    private var isDiscovering = false

    override suspend fun advertise(advertiserInfo: AdvertiserInfo) {
        stopAdvertising()

        val infoByteArray = gson.toJson(advertiserInfo).toByteArray()
        client.startAdvertising(
            infoByteArray,
            serviceId,
            connectionCallback,
            advertisingOptions
        ).await()
        isAdvertising = true
    }

    override fun stopAdvertising() {
        if (isAdvertising) {
            client.stopAdvertising()
            isAdvertising = false
        }
    }

    override suspend fun discover() {
        if (!isDiscovering) {
            client.startDiscovery(serviceId, discoveryCallback, discoveryOperations).await()
            isDiscovering = true
        }
    }

    override fun stopDiscovery() {
        if (isDiscovering) {
            client.stopDiscovery()
            isDiscovering = false
        }
    }

    private inner class ConnectionCallback : ConnectionLifecycleCallback() {

        override fun onConnectionInitiated(endpointId: String, info: ConnectionInfo) {

        }

        override fun onConnectionResult(endpointId: String, resolution: ConnectionResolution) {

        }

        override fun onDisconnected(endpointId: String) {

        }
    }

    private inner class DiscoveryCallback : EndpointDiscoveryCallback() {

        override fun onEndpointFound(endpointId: String, info: DiscoveredEndpointInfo) {

        }

        override fun onEndpointLost(endpointId: String) {

        }
    }
}