package io.flaterlab.data

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName


data class HostInfo(
    @Expose @SerializedName("endpointId") val endpointId: String,
)