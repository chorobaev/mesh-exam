package io.flaterlab.meshexam.library.nearby.impl

import com.google.gson.annotations.Expose

data class AdvertiserInfo(
    @Expose val hostName: String,
    @Expose val examId: String,
    @Expose val examName: String,
    @Expose val examDuration: Long,
)