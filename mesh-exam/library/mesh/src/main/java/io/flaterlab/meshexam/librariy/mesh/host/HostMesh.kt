package io.flaterlab.meshexam.librariy.mesh.host

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

data class AdvertiserInfo(
    @Expose val hostName: String,
    @Expose val examId: String,
    @Expose val examName: String,
    @Expose val examDuration: Long,
)