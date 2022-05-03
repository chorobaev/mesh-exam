package io.flaterlab.meshexam.data.datastore.entity

import com.google.gson.annotations.Expose

internal data class AppInfoEntity(
    @Expose val isFirstStartUp: Boolean,
)