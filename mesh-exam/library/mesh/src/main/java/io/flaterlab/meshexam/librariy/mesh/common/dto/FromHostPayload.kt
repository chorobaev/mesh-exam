package io.flaterlab.meshexam.librariy.mesh.common.dto

import com.google.gson.annotations.Expose

data class FromHostPayload(
    @Expose val type: String,
    @Expose val data: String,
    @Expose val targetClientId: String? = null,
)

data class StartExamPayload(
    @Expose val examId: String,
) {

    companion object {
        const val TYPE_KEY = "START_EXAM"
    }
}