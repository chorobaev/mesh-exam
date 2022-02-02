package io.flaterlab.meshexam.librariy.mesh.common.dto

import com.google.gson.annotations.Expose

data class ClientMesh(
    val advertiserList: List<AdvertiserInfo>,
)

data class AdvertiserInfo(
    @Expose val hostName: String,
    @Expose val examId: String,
    @Expose val examName: String,
    @Expose val examDuration: Long,
)

internal typealias ParentInfo = Pair<String, AdvertiserInfo>