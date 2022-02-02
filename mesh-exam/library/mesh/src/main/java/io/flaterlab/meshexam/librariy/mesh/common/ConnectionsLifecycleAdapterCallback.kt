package io.flaterlab.meshexam.librariy.mesh.common

import com.google.android.gms.nearby.connection.ConnectionInfo
import com.google.android.gms.nearby.connection.ConnectionLifecycleCallback
import com.google.android.gms.nearby.connection.ConnectionResolution
import com.google.android.gms.nearby.connection.ConnectionsStatusCodes
import com.google.gson.JsonSyntaxException
import io.flaterlab.meshexam.librariy.mesh.common.parser.JsonParser
import timber.log.Timber

internal class ConnectionsLifecycleAdapterCallback<T>(
    private val jsonParser: JsonParser<T>,
) : ConnectionLifecycleCallback() {

    var adapterCallback: AdapterCallback<T>? = null

    override fun onConnectionInitiated(endpointId: String, info: ConnectionInfo) {
        try {
            val json = String(info.endpointInfo)
            val clientInfo = jsonParser.fromJson(json)
            adapterCallback?.onRequested(endpointId, clientInfo)
        } catch (e: JsonSyntaxException) {
            Timber.e(e)
            adapterCallback?.rejectConnection(endpointId)
        }
    }

    override fun onConnectionResult(
        endpointId: String,
        resolution: ConnectionResolution
    ) {
        when (resolution.status.statusCode) {
            ConnectionsStatusCodes.STATUS_OK ->
                adapterCallback?.onConnected(endpointId)
            ConnectionsStatusCodes.STATUS_CONNECTION_REJECTED ->
                adapterCallback?.onRejected(endpointId)
            ConnectionsStatusCodes.STATUS_ERROR ->
                adapterCallback?.onError(endpointId)
        }
    }

    override fun onDisconnected(endpointId: String) {
        adapterCallback?.onDisconnected(endpointId)
    }

    interface AdapterCallback<T> {

        fun onRequested(endpointId: String, info: T)
        fun onConnected(endpointId: String)
        fun onRejected(endpointId: String)
        fun onError(endpointId: String)
        fun onDisconnected(endpointId: String)
        fun rejectConnection(endpointId: String)
    }
}