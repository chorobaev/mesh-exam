package io.flaterlab.data

import android.content.Context
import com.google.android.gms.nearby.Nearby
import com.google.android.gms.nearby.messages.NearbyMessagesStatusCodes
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject

class LocalDataSource @Inject constructor(
    @ApplicationContext context: Context,
) {

    private val client = Nearby.getConnectionsClient(context)

    fun connect() {

    }
}