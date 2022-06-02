package io.flaterlab.meshexam.data.datasource

import com.google.gson.Gson
import io.flaterlab.meshexam.core.Mapper
import io.flaterlab.meshexam.data.communication.MeshMessage
import io.flaterlab.meshexam.data.communication.fromClient.AttemptDto
import io.flaterlab.meshexam.data.communication.fromClient.AttemptDtoProvider
import io.flaterlab.meshexam.data.datastore.dao.UserProfileDao
import io.flaterlab.meshexam.domain.datasource.ResultSenderDataSource
import io.flaterlab.meshexam.librariy.mesh.common.dto.FromClientPayload
import io.flaterlab.meshexam.library.messaging.Message
import io.flaterlab.meshexam.library.messaging.MessagingFacade
import io.flaterlab.meshexam.library.messaging.SenderInfo
import kotlinx.coroutines.flow.first
import javax.inject.Inject

internal class ResultSenderDataSourceImpl @Inject constructor(
    private val messaging: MessagingFacade,
    private val userProfileDao: UserProfileDao,
    private val attemptDtoProvider: AttemptDtoProvider,
    private val clientMessageMapper: Mapper<MeshMessage, FromClientPayload>,
    private val gson: Gson,
) : ResultSenderDataSource {

    override suspend fun sendAttemptResult(attemptId: String) {
        val userProfile = userProfileDao.userProfile().first()
        val attemptDto = attemptDtoProvider.provide(attemptId)

        val senderInfo = SenderInfo(userProfile.id)
        val payload = Message(
            senderInfo = senderInfo,
            receiverId = attemptDto.hostingId,
            content = provideContent(attemptDto)
        )
        messaging.sendMessage(payload)
    }

    private fun provideContent(attemptDto: AttemptDto): String {
        val fromClientPayload = clientMessageMapper(attemptDto)
        return gson.toJson(fromClientPayload)
    }
}