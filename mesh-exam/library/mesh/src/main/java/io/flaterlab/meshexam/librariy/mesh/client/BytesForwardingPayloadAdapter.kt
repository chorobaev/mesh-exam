package io.flaterlab.meshexam.librariy.mesh.client

import com.google.android.gms.nearby.connection.Payload
import com.google.android.gms.nearby.connection.PayloadCallback
import com.google.android.gms.nearby.connection.PayloadTransferUpdate

internal class BytesForwardingPayloadAdapter : PayloadCallback() {

    var onBytesReceivedListener: (ByteArray) -> Unit = {}

    override fun onPayloadReceived(endpointId: String, payload: Payload) {
        if (payload.type == Payload.Type.BYTES) {
            payload.asBytes()?.also(onBytesReceivedListener)
        }
    }

    override fun onPayloadTransferUpdate(endpointId: String, update: PayloadTransferUpdate) = Unit
}