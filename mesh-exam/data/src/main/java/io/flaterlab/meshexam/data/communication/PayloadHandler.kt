package io.flaterlab.meshexam.data.communication

import javax.inject.Inject
import javax.inject.Singleton

@Singleton
internal class PayloadHandler<M> @Inject constructor(
    handlerSet: Set<@JvmSuppressWildcards Handler<M>>
) {

    private val handlers = ArrayList<Handler<M>>(handlerSet)

    fun add(handler: Handler<M>) = handlers.add(handler)

    suspend fun handle(payload: M) {
        for (handler in handlers) {
            handler.handle(payload)
        }
    }

    fun interface Handler<M> {

        suspend fun handle(payload: M): Boolean
    }
}