package io.flaterlab.feature.main

import android.content.Context
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.android_base.SingleLiveEvent
import com.google.android.gms.nearby.Nearby
import com.google.android.gms.nearby.connection.*
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import io.flaterlab.feature.main.message.MessageDvo
import java.util.*
import javax.inject.Inject

@HiltViewModel
class NearbyViewModel @Inject constructor(
    @ApplicationContext context: Context,
) : ViewModel() {

    val nameEntered = MutableLiveData(false)
    val advertising = MutableLiveData(false)
    val discovering = MutableLiveData(false)
    val message = SingleLiveEvent<String>()
    val connectedPhone = MutableLiveData<String?>(null)
    val availableList = MutableLiveData<List<HostDvo>>(emptyList())
    val messages = MutableLiveData<List<MessageDvo>>(emptyList())

    private val endpoints: MutableMap<String, HostDvo> = TreeMap()
    private var name = ""

    private val nearby = Nearby.getConnectionsClient(context)
    private val hostConnectionLifecycleCallback = object : ConnectionLifecycleCallback() {
        override fun onConnectionInitiated(endpointId: String, info: ConnectionInfo) {
            nearby.acceptConnection(endpointId, payloadCallback)
        }

        override fun onConnectionResult(endpointId: String, result: ConnectionResolution) {
            when (result.status.statusCode) {
                ConnectionsStatusCodes.STATUS_OK -> connectedPhone.value = endpointId
                ConnectionsStatusCodes.STATUS_CONNECTION_REJECTED -> connectedPhone.value = null
                ConnectionsStatusCodes.STATUS_ERROR -> connectedPhone.value = null
            }
        }

        override fun onDisconnected(endpointId: String) {
            if (endpointId == connectedPhone.value) connectedPhone.value = null
        }
    }
    private val clientConnectionLifecycleCallback = object : ConnectionLifecycleCallback() {
        override fun onConnectionInitiated(endpointId: String, info: ConnectionInfo) {
            nearby.acceptConnection(endpointId, payloadCallback)
        }

        override fun onConnectionResult(endpointId: String, result: ConnectionResolution) {
            when (result.status.statusCode) {
                ConnectionsStatusCodes.STATUS_OK -> connectedPhone.value = endpointId
                ConnectionsStatusCodes.STATUS_CONNECTION_REJECTED -> connectedPhone.value = null
                ConnectionsStatusCodes.STATUS_ERROR -> connectedPhone.value = null
            }
        }

        override fun onDisconnected(endpointId: String) {
            if (endpointId == connectedPhone.value) connectedPhone.value = null
        }
    }
    private val payloadCallback = object : PayloadCallback() {
        override fun onPayloadReceived(endpointId: String, payload: Payload) {
            if (payload.type == Payload.Type.BYTES) {
                val msg = payload.asBytes()?.let(::String) ?: return
                messages.value = messages.value.orEmpty() + MessageDvo(msg)
            }
        }

        override fun onPayloadTransferUpdate(endpointId: String, update: PayloadTransferUpdate) {

        }
    }

    private val endpointDiscoveryCallback = object : EndpointDiscoveryCallback() {
        override fun onEndpointFound(endpointId: String, info: DiscoveredEndpointInfo) {
            endpoints[endpointId] = HostDvo(endpointId, info.serviceId, info.endpointName)
            availableList.value = endpoints.values.toList()
        }

        override fun onEndpointLost(endpointId: String) {
            endpoints.remove(endpointId)
            availableList.value = endpoints.values.toList()
        }
    }

    fun onAdvertiseClick() {
        if (advertising.value == true) {
            stopAdvertising()
        } else {
            startAdvertising()
        }
    }

    private fun startAdvertising() {
        val options = AdvertisingOptions.Builder()
            .setStrategy(Strategy.P2P_CLUSTER)
            .build()
        nearby
            .startAdvertising(
                getLocalUserName(),
                getServiceId(),
                hostConnectionLifecycleCallback,
                options
            )
            .addOnSuccessListener {
                advertising.value = true
            }
            .addOnFailureListener {
                advertising.value = false
            }
    }

    private fun stopAdvertising() {
        nearby
            .stopAdvertising()
        advertising.value = false
    }

    fun onDiscoverClicked() {
        if (discovering.value == true) {
            stopDiscovering()
            availableList.value = emptyList()
        } else {
            startDiscovering()
        }
    }

    private fun startDiscovering() {
        val options = DiscoveryOptions.Builder()
            .setStrategy(Strategy.P2P_CLUSTER)
            .build()
        nearby
            .startDiscovery(
                getServiceId(),
                endpointDiscoveryCallback,
                options
            )
            .addOnSuccessListener {
                discovering.value = true
            }
            .addOnFailureListener {
                discovering.value = false
            }
    }

    private fun stopDiscovering() {
        nearby.stopDiscovery()
        discovering.value = false
    }

    private fun getLocalUserName() = name

    private fun getServiceId() = "MeshExamTest"

    fun onNameChanged(name: String) {
        this.name = name
        nameEntered.value = name.isNotBlank()
    }

    fun onItemClicked(host: HostDvo) {
        nearby
            .requestConnection(getLocalUserName(), host.hostId, clientConnectionLifecycleCallback)
            .addOnSuccessListener {
                message.setValue("Connection requested!")
            }
            .addOnFailureListener {
                message.setValue("Connection request filed!")
            }
    }

    fun onSendClicked(msg: String) {
        connectedPhone.value?.let { endpointId ->
            val payload = Payload.fromBytes(msg.toByteArray())
            nearby.sendPayload(endpointId, payload)
                .addOnFailureListener {
                    message.setValue("Message didn't send :(")
                }
        }
    }
}