package io.flaterlab.meshexam.test.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.google.android.gms.nearby.Nearby
import com.google.android.gms.nearby.connection.*
import com.google.gson.Gson
import io.flaterlab.meshexam.librariy.mesh.common.dto.ClientInfo
import io.flaterlab.meshexam.test.R
import kotlinx.android.synthetic.main.fragment_connect.*
import timber.log.Timber
import java.util.*

class ConnectFragment : Fragment() {

    private val client get() = Nearby.getConnectionsClient(requireContext())
    private val serviceId get() = requireContext().packageName
    private val advertiser = "Advertiser"

    private val payloadCallback = object : PayloadCallback() {

        override fun onPayloadReceived(endpointId: String, payload: Payload) {
            if (payload.type == Payload.Type.BYTES) {
                payload.asBytes()?.let { data ->

                }
            }
        }

        override fun onPayloadTransferUpdate(endpointId: String, update: PayloadTransferUpdate) {
        }
    }

    private val advertiserCallback = object : ConnectionLifecycleCallback() {

        override fun onConnectionInitiated(endpointId: String, info: ConnectionInfo) {
            printThread("Discoverer connection initiated")
            client.acceptConnection(endpointId, payloadCallback)
        }

        override fun onConnectionResult(
            endpointId: String,
            resolution: ConnectionResolution
        ) {
            printThread("Discoverer connection result (success = ${resolution.status.isSuccess})")
            when (resolution.status.statusCode) {
                ConnectionsStatusCodes.STATUS_OK -> {
                    Toast.makeText(requireContext(), "Connected to $endpointId", Toast.LENGTH_SHORT)
                        .show()
                }
                ConnectionsStatusCodes.STATUS_CONNECTION_REJECTED -> Unit
                ConnectionsStatusCodes.STATUS_ERROR -> Unit
            }
        }

        override fun onDisconnected(endpointId: String) {
            printThread("Discoverer disconnected")
        }
    }

    private val discovererCallback = object : ConnectionLifecycleCallback() {

        override fun onConnectionInitiated(endpointId: String, info: ConnectionInfo) {
            printThread("Advertiser connection initiated")
            client.acceptConnection(endpointId, payloadCallback)
        }

        override fun onConnectionResult(
            endpointId: String,
            resolution: ConnectionResolution
        ) {
            printThread("Advertiser connection result (success = ${resolution.status.isSuccess})")
            when (resolution.status.statusCode) {
                ConnectionsStatusCodes.STATUS_OK -> {
                    Toast.makeText(requireContext(), "Connected to $endpointId", Toast.LENGTH_SHORT)
                        .show()
                }
                ConnectionsStatusCodes.STATUS_CONNECTION_REJECTED -> Unit
                ConnectionsStatusCodes.STATUS_ERROR -> Unit
            }
        }

        override fun onDisconnected(endpointId: String) {
            printThread("Advertiser disconnected")
        }
    }

    private val endpointDiscoveryCallback = object : EndpointDiscoveryCallback() {

        override fun onEndpointFound(endpointId: String, info: DiscoveredEndpointInfo) {
            printThread("Endpoint found")
            linear_layout.run {
                removeAllViews()
                val text = layoutInflater.inflate(R.layout.item_text, this, false) as TextView
                text.text = "$endpointId: ${info.endpointName}"
                text.setOnClickListener {
                    Toast.makeText(
                        requireContext(),
                        "Connecting to ${text.text}",
                        Toast.LENGTH_SHORT
                    ).show()
                    val clientInfo = ClientInfo(
                        UUID.randomUUID().toString(),
                        "Discoverer",
                        "COM-18",
                        0
                    ).let(Gson()::toJson).toByteArray()
                    client.requestConnection(clientInfo, endpointId, discovererCallback)
                        .addOnSuccessListener {
                            text.isEnabled = false
                            client.stopDiscovery()
                        }
                }
                addView(text)
            }
        }

        override fun onEndpointLost(endpointId: String) {
            printThread("Endpoint lost")
        }
    }

    private var isAdvertising = false
        set(value) {
            btn_advertise.text = if (value) "Advertising..." else "Advertise"
            field = value
        }

    private var isDiscovering = false
        set(value) {
            btn_discover.text = if (value) "Discovering..." else "Discover"
            field = value
        }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_connect, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        btn_advertise.setOnClickListener {
            if (isAdvertising) {
                client.stopAdvertising()
                isAdvertising = false
            } else {
                client.startAdvertising(
                    advertiser,
                    serviceId,
                    advertiserCallback,
                    AdvertisingOptions.Builder()
                        .setDisruptiveUpgrade(false)
                        .setStrategy(Strategy.P2P_CLUSTER)
                        .build()
                )
                    .addOnSuccessListener {
                        printThread("Advertising success")
                        isAdvertising = true
                    }
            }
        }

        btn_discover.setOnClickListener {
            if (isDiscovering) {
                client.stopDiscovery()
                isDiscovering = false
            } else {
                client.startDiscovery(
                    serviceId,
                    endpointDiscoveryCallback,
                    DiscoveryOptions.Builder()
                        .setStrategy(Strategy.P2P_CLUSTER)
                        .build()
                )
                    .addOnSuccessListener {
                        printThread("Discovery success")
                        isDiscovering = true
                    }
            }
        }
    }

    fun printThread(tag: String) {
        Timber.d("$tag: ${Thread.currentThread().name}")
    }
}