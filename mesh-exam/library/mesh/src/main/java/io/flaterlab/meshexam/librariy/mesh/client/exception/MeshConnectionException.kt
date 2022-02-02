package io.flaterlab.meshexam.librariy.mesh.client.exception

class MeshConnectionException(
    cause: Throwable? = null,
) : Exception("Mesh connection error", cause)