package io.flaterlab.meshexam.data.datasource

import com.google.gson.Gson
import io.flaterlab.meshexam.data.communication.PayloadHandler
import io.flaterlab.meshexam.data.database.MeshDatabase
import io.flaterlab.meshexam.domain.datasource.ResultReceiverDataSource
import io.flaterlab.meshexam.domain.results.SenderItemModel
import io.flaterlab.meshexam.librariy.mesh.common.dto.FromClientPayload
import io.flaterlab.meshexam.library.messaging.Message
import io.flaterlab.meshexam.library.messaging.MessagingFacade
import io.flaterlab.meshexam.library.messaging.ReceiverInfo
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.*
import timber.log.Timber
import javax.inject.Inject

internal class ResultReceiverDataSourceImpl @Inject constructor(
    database: MeshDatabase,
    private val messaging: MessagingFacade,
    private val attemptDtoHandler: PayloadHandler.Handler<FromClientPayload>,
    private val gson: Gson,
) : ResultReceiverDataSource {

    private val userDao = database.userDao()

    private val senders = MutableStateFlow<List<SenderItemModel>>(emptyList())

    @OptIn(ExperimentalCoroutinesApi::class)
    override fun startAcceptingResults(hostingId: String): Flow<List<SenderItemModel>> {
        val receiverInfo = ReceiverInfo(hostingId)
        return messaging.startReceiving(receiverInfo)
            .onEach { message ->
                onPayloadReceived(message, hostingId)
            }
            .onCompletion { senders.value = emptyList() }
            .flatMapLatest { senders }
    }

    private suspend fun onPayloadReceived(payload: Message, hostingId: String) {
        Timber.d("Payload received, payload = $payload, hostingId = $hostingId")
        if (payload.receiverId == hostingId) {
            saveContent(payload.content)
            saveUser(payload.senderInfo.uid)
        }
    }

    private suspend fun saveContent(content: String) {
        Timber.d("Saving content, content = $content")
        val payload = gson.fromJson(content, FromClientPayload::class.java)
        attemptDtoHandler.handle(payload)
    }

    private suspend fun saveUser(uid: String) {
        val user = userDao.getUserById(uid)
        val sender = SenderItemModel(
            id = uid,
            studentFullName = user.fullName,
            studentInfo = user.info,
            status = SenderItemModel.Status.RECEIVED,
            grade = 0F,
            totalGrade = 0,
        )
        senders.value = senders.value + sender
    }
}