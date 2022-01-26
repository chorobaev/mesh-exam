package io.flaterlab.meshexam.library.nearby.impl

import com.google.android.gms.nearby.connection.ConnectionInfo
import com.google.android.gms.nearby.connection.ConnectionLifecycleCallback
import com.google.android.gms.nearby.connection.ConnectionResolution
import com.google.android.gms.nearby.connection.ConnectionsStatusCodes
import timber.log.Timber


internal class MeshConnectionCallback<M>(
    private val jsonParser: (String) -> M,
    private val onEvent: (ConnectionResult<M>) -> Unit = {},
) : ConnectionLifecycleCallback() {

    override fun onConnectionInitiated(endpointId: String, info: ConnectionInfo) {
        try {
            val json = String(info.endpointInfo)
            onEvent(ConnectionResult.Request(endpointId, jsonParser(json)))
        } catch (ignored: Exception) {
            Timber.e(ignored)
        }
    }

    override fun onConnectionResult(
        endpointId: String,
        resolution: ConnectionResolution
    ) {
        when (resolution.status.statusCode) {
            ConnectionsStatusCodes.STATUS_OK ->
                onEvent(ConnectionResult.Connected(endpointId))
            ConnectionsStatusCodes.STATUS_CONNECTION_REJECTED ->
                onEvent(ConnectionResult.Rejected(endpointId))
            ConnectionsStatusCodes.STATUS_ERROR ->
                onEvent(ConnectionResult.Error())
        }
    }

    override fun onDisconnected(endpointId: String) {
        onEvent(ConnectionResult.Disconnected(endpointId))
    }
}
