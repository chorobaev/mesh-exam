package io.flaterlab.meshexam.librariy.mesh.client

import com.google.android.gms.nearby.connection.ConnectionsClient

internal class ClientMeshManager(
    private val serviceId: String,
    private val client: ConnectionsClient,
) {

    var isConnected: Boolean = false; private set
}