package io.flaterlab.meshexam.librariy.mesh.common

import com.google.android.gms.nearby.connection.Payload
import com.google.android.gms.nearby.connection.PayloadCallback
import com.google.android.gms.nearby.connection.PayloadTransferUpdate
import com.google.gson.Gson
import com.google.gson.JsonSyntaxException
import io.flaterlab.meshexam.librariy.mesh.common.dto.MeshData
import io.flaterlab.meshexam.librariy.mesh.common.dto.MeshPayload
import timber.log.Timber

internal class PayloadAdapterCallback(
    private val gson: Gson
) : PayloadCallback() {

    var adapterCallback: AdapterCallback? = null

    override fun onPayloadReceived(endpointId: String, payload: Payload) {
        if (payload.type == Payload.Type.BYTES) {
            payload.asBytes()?.also(::onByteArrayReceived)
        }
    }

    override fun onPayloadTransferUpdate(endpointId: String, update: PayloadTransferUpdate) = Unit

    private fun onByteArrayReceived(bytes: ByteArray) {
        val json = String(bytes)
        Timber.d("On payload received: $json")
        try {
            val payload = gson.fromJson(json, MeshPayload::class.java)
            MeshPayload.ContentType.values()
                .find { it == payload.type }
                ?.let { contentType ->
                    gson.fromJson(payload.data, contentType.classType.java)
                }
                ?.also(::onDataReceived)
        } catch (ignored: JsonSyntaxException) {
            Timber.e(ignored)
        }
    }

    private fun onDataReceived(data: MeshData) {
        when (data) {
            is MeshData.ClientConnected -> adapterCallback?.onClientConnected(data)
        }
    }

    interface AdapterCallback {

        fun onClientConnected(data: MeshData.ClientConnected) = Unit
    }
}