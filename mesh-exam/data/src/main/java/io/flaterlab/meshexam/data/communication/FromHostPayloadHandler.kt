package io.flaterlab.meshexam.data.communication

import io.flaterlab.meshexam.librariy.mesh.common.dto.FromHostPayload
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
internal class FromHostPayloadHandler @Inject constructor(
    handlerSet: Set<@JvmSuppressWildcards Handler>
) {

    private val handlers = ArrayList<Handler>(handlerSet)

    fun add(handler: Handler) = handlers.add(handler)

    suspend fun handle(payload: FromHostPayload) {
        for (handler in handlers) {
            handler.handle(payload)
        }
    }

    fun interface Handler {

        suspend fun handle(payload: FromHostPayload): Boolean
    }
}