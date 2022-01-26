package io.flaterlab.meshexam.library.nearby.impl

import com.google.android.gms.nearby.connection.*
import io.flaterlab.meshexam.library.nearby.api.NearbyFacade
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.tasks.await

internal class NearbyFacadeImpl(
    private val serviceId: String,
    private val client: ConnectionsClient,
    private val advertiserInfoParser: JsonParser<AdvertiserInfo>,
    private val clientInfoParser: JsonParser<ClientInfo>,
) : NearbyFacade {

    private val strategy: Strategy = Strategy.P2P_CLUSTER

    override fun advertise(
        advertiserInfo: AdvertiserInfo
    ): Flow<ConnectionResult<ClientInfo>> = callbackFlow {
        val infoByteArray = advertiserInfoParser.toJson(advertiserInfo).toByteArray()
        val callback = MeshConnectionCallback(clientInfoParser::fromJson, ::trySend)
        val advertisingOptions = AdvertisingOptions.Builder()
            .setStrategy(strategy)
            .setDisruptiveUpgrade(false)
            .build()
        client.startAdvertising(
            infoByteArray,
            serviceId,
            callback,
            advertisingOptions
        ).await()
        awaitClose(client::stopAdvertising)
    }

    override fun discover(): Flow<List<Pair<String, AdvertiserInfo>>> = callbackFlow {
        val callback = DiscoveryCallback(advertiserInfoParser::fromJson) { advertiserMap ->
            trySend(advertiserMap.toList())
        }
        val discoveryOperations = DiscoveryOptions.Builder()
            .setStrategy(strategy)
            .build()
        client
            .startDiscovery(serviceId, callback, discoveryOperations)
            .await()
        awaitClose(client::stopDiscovery)
    }

    override fun connect(
        endpointId: String,
        clientInfo: ClientInfo
    ): Flow<ConnectionResult<AdvertiserInfo>> = callbackFlow {
        val infoByteArray = clientInfoParser.toJson(clientInfo).toByteArray()
        val callback = MeshConnectionCallback(advertiserInfoParser::fromJson, ::trySend)
        client.requestConnection(infoByteArray, endpointId, callback).await()
        awaitClose { }
    }

    override fun acceptConnection(endpointId: String): Flow<ByteArray> = callbackFlow {
        val callback = MeshPayloadCallback(::trySend)
        client.acceptConnection(endpointId, callback).await()
        awaitClose { client.disconnectFromEndpoint(endpointId) }
    }

    override suspend fun sendPayload(vararg toEndpointId: String, data: ByteArray) {
        client.sendPayload(toEndpointId.toList(), Payload.fromBytes(data)).await()
    }

    interface JsonParser<T> {

        fun fromJson(json: String): T

        fun toJson(model: T): String
    }
}