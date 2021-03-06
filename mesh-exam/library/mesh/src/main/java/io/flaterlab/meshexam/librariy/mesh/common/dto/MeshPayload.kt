package io.flaterlab.meshexam.librariy.mesh.common.dto

import com.google.gson.annotations.Expose
import kotlin.reflect.KClass

sealed class MeshData {

    data class ClientConnected(
        @Expose val clientInfo: ClientInfo,
        @Expose val parentId: String,
    ) : MeshData()

    data class ClientDisconnected(
        @Expose val clientInfo: ClientInfo
    ) : MeshData()
}

data class MeshPayload(
    @Expose val type: ContentType,
    @Expose val data: String,
) {

    enum class ContentType(
        val classType: KClass<out MeshData>,
    ) {
        CLIENT_CONNECTED(MeshData.ClientConnected::class),
        CLIENT_DISCONNECTED(MeshData.ClientDisconnected::class),
        CLIENT_PAYLOAD(FromClientPayload::class),
        ;
    }
}