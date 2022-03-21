package io.flaterlab.meshexam.librariy.mesh.common.dto

import com.google.gson.annotations.Expose

data class FromClientPayload(
    @Expose val contentType: String,
    @Expose val data: String,
): MeshData()