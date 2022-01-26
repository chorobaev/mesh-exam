package io.flaterlab.meshexam.library.nearby.impl

sealed class ConnectionResult<M> {
    data class Request<M>(
        val endpointId: String,
        val info: M,
    ): ConnectionResult<M>()

    data class Connected<M>(
        val endpointId: String,
    ): ConnectionResult<M>()

    data class Rejected<M>(
        val endpointId: String
    ): ConnectionResult<M>()

    data class Error<M>(
        val cause: Throwable? = null,
    ): ConnectionResult<M>()

    data class Disconnected<M>(
        val endpointId: String,
    ): ConnectionResult<M>()
}