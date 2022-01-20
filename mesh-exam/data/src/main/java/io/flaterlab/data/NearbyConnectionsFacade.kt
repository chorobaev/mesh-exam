package io.flaterlab.data

import com.google.android.gms.nearby.connection.*
import com.google.gson.Gson
import com.google.gson.stream.MalformedJsonException
import kotlinx.coroutines.tasks.await
import timber.log.Timber
import java.util.*

interface NearbyConnectionsFacade {

    suspend fun startAdvertising()

    fun stopAdvertising()

    suspend fun startDiscovery()

    fun stopDiscovery()

    fun setHostDiscoveryCallback(callback: HostDiscoveryCallback)

    fun setConnectionCallback(callback: ConnectionCallback)

    interface HostDiscoveryCallback {

        fun onHostFound(hostInfo: HostInfo)

        fun onHostLost(hostInfo: HostInfo)
    }

    interface ConnectionCallback {

    }
}

internal class NearbyConnectionFacadeImpl(
    private val userId: String,
    private val serviceId: String,
    private val client: ConnectionsClient,
    private val gson: Gson,
) : NearbyConnectionsFacade {

    private val connectionLifecycleCallback = object : ConnectionLifecycleCallback() {

        override fun onConnectionInitiated(endpointId: String, info: ConnectionInfo) {
            
        }

        override fun onConnectionResult(endpointId: String, result: ConnectionResolution) {

        }

        override fun onDisconnected(endpointId: String) {

        }
    }

    private val endpointDiscoveryCallback = object : EndpointDiscoveryCallback() {

        override fun onEndpointFound(endpointId: String, info: DiscoveredEndpointInfo) {
            Timber.d("Endpoint found: endpointId = $endpointId, name = ${info.endpointName}")
            val hostInfo: HostInfo = try {
                gson.fromJson(String(info.endpointInfo), HostInfo::class.java) ?: return
            } catch (ex: MalformedJsonException) {
                Timber.e(ex)
                return
            }
            _hostMap[endpointId] = hostInfo
            _hostDiscoveryCallback?.onHostFound(hostInfo)
        }

        override fun onEndpointLost(endpointId: String) {
            Timber.d("Host lost: endpointId: $endpointId")
            _hostMap[endpointId]?.let { _hostDiscoveryCallback?.onHostLost(it) }
        }
    }
    private val _hostMap: MutableMap<String, HostInfo> = TreeMap()
    private var _hostDiscoveryCallback: NearbyConnectionsFacade.HostDiscoveryCallback? = null
    private var _connectionCallback: NearbyConnectionsFacade.ConnectionCallback? = null

    override suspend fun startAdvertising() {
        val info: ByteArray = gson.toJson(getHostInfo()).toByteArray()
        val options = AdvertisingOptions.Builder()
            .setStrategy(Strategy.P2P_CLUSTER)
            .setDisruptiveUpgrade(true)
            .build()
        client
            .startAdvertising(info, serviceId, connectionLifecycleCallback, options)
            .await()
    }

    private fun getHostInfo() = HostInfo(userId)

    override fun stopAdvertising() = client.stopAdvertising()

    override suspend fun startDiscovery() {
        val options = DiscoveryOptions.Builder()
            .setStrategy(Strategy.P2P_CLUSTER)
            .build()
        client
            .startDiscovery(serviceId, endpointDiscoveryCallback, options)
            .await()
    }

    override fun stopDiscovery() = client.stopDiscovery()

    override fun setHostDiscoveryCallback(callback: NearbyConnectionsFacade.HostDiscoveryCallback) {
        _hostDiscoveryCallback = callback
    }

    override fun setConnectionCallback(callback: NearbyConnectionsFacade.ConnectionCallback) {
        _connectionCallback = callback
    }
}