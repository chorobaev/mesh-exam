package io.flaterlab.meshexam.librariy.mesh.common.dto

import com.google.gson.annotations.Expose

data class HostMesh(
    val clientList: List<ClientInfo>,
)

data class ClientInfo(
    @Expose val id: String,
    @Expose val name: String,
    @Expose val info: String,
    @Expose val status: String,
)

internal typealias ChildInfo = Pair<String, ClientInfo>