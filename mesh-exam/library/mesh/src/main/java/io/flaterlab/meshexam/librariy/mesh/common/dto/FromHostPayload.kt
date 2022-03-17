package io.flaterlab.meshexam.librariy.mesh.common.dto

import com.google.gson.annotations.Expose

data class FromHostPayload(
    @Expose val contentType: String,
    @Expose val data: String,
    @Expose val targetClientId: String? = null,
)