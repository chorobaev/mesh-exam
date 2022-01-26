package io.flaterlab.meshexam.library.nearby.api

import io.flaterlab.meshexam.library.nearby.impl.AdvertiserInfo
import io.flaterlab.meshexam.library.nearby.impl.ClientInfo
import io.flaterlab.meshexam.library.nearby.impl.ConnectionResult
import kotlinx.coroutines.flow.Flow

interface NearbyFacade {

    fun advertise(advertiserInfo: AdvertiserInfo): Flow<ConnectionResult<ClientInfo>>

    fun discover(): Flow<List<Pair<String, AdvertiserInfo>>>

    fun connect(endpointId: String, clientInfo: ClientInfo): Flow<ConnectionResult<AdvertiserInfo>>

    fun acceptConnection(endpointId: String): Flow<ByteArray>

    suspend fun sendPayload(vararg toEndpointId: String, data: ByteArray)
}