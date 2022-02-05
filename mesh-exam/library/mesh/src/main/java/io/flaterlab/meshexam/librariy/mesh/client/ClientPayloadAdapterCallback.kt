package io.flaterlab.meshexam.librariy.mesh.client

import com.google.android.gms.nearby.connection.Payload
import com.google.android.gms.nearby.connection.PayloadCallback
import com.google.android.gms.nearby.connection.PayloadTransferUpdate
import com.google.gson.Gson

internal class ClientPayloadAdapterCallback(
    private val gson: Gson
) : PayloadCallback() {

    override fun onPayloadReceived(endpointId: String, payload: Payload) {
        if (payload.type == Payload.Type.BYTES) {
            payload.asBytes()?.also(::onByteArrayReceived)
        }
    }

    override fun onPayloadTransferUpdate(endpointId: String, update: PayloadTransferUpdate) = Unit

    private fun onByteArrayReceived(bytes: ByteArray) {

    }
}