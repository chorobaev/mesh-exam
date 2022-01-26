package io.flaterlab.meshexam.library.nearby.impl

import com.google.android.gms.nearby.connection.Payload
import com.google.android.gms.nearby.connection.PayloadCallback
import com.google.android.gms.nearby.connection.PayloadTransferUpdate

internal class MeshPayloadCallback(
    private val onDataReceived: (ByteArray) -> Unit,
) : PayloadCallback() {

    override fun onPayloadReceived(endpointId: String, payload: Payload) {
        if (payload.type == Payload.Type.BYTES) {
            payload.asBytes()?.also(onDataReceived)
        }
    }

    override fun onPayloadTransferUpdate(endpointId: String, update: PayloadTransferUpdate) = Unit
}