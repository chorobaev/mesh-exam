package io.flaterlab.meshexam.library.messaging

import android.content.Context
import com.google.android.gms.nearby.Nearby
import com.google.gson.Gson
import kotlinx.coroutines.flow.Flow
import javax.inject.Provider

interface MessagingFacade {

    fun startReceiving(info: ReceiverInfo): Flow<Message>

    suspend fun sendMessage(message: Message)

    companion object {
        @Volatile
        private var instance: MessagingFacade? = null

        fun getInstance(context: Context): MessagingFacade =
            instance ?: synchronized(this) {
                instance ?: Pair(
                    Nearby.getConnectionsClient(context.applicationContext),
                    Gson()
                ).let { (nearby, gson) ->
                    val serviceId = context.packageName + ".messaging"
                    MessagingImpl(
                        { Sender(serviceId, nearby, gson) },
                        { Receiver(serviceId, nearby, gson) },
                    )
                }.also { instance = it }
            }
    }
}

internal class MessagingImpl(
    private val senderProvider: Provider<Sender>,
    private val receiverProvider: Provider<Receiver>,
) : MessagingFacade {

    override fun startReceiving(info: ReceiverInfo): Flow<Message> =
        receiverProvider.get().startReceiving(info)

    override suspend fun sendMessage(message: Message) =
        senderProvider.get().sendMessage(message)
}