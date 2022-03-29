package io.flaterlab.meshexam.data.communication.fromHost

import com.google.gson.Gson
import io.flaterlab.meshexam.data.communication.PayloadHandler
import io.flaterlab.meshexam.data.database.MeshDatabase
import io.flaterlab.meshexam.domain.repository.AttemptRepository
import io.flaterlab.meshexam.librariy.mesh.common.dto.FromHostPayload
import timber.log.Timber
import javax.inject.Inject

internal class FinishExamEventPayloadHandler @Inject constructor(
    database: MeshDatabase,
    private val gson: Gson,
    private val attemptRepository: AttemptRepository,
) : PayloadHandler.Handler<FromHostPayload> {

    private val attemptDao = database.attemptDao()

    override suspend fun handle(payload: FromHostPayload): Boolean {
        if (payload.contentType != FinishExamEventDto.contentType) return false
        val eventDto = gson.fromJson(payload.data, FinishExamEventDto::class.java)
        submitExam(eventDto.hostingId)
        return true
    }

    private suspend fun submitExam(hostingId: String) {
        val attempt = attemptDao.getClientAttemptByHostingId(hostingId).also {
            Timber.d("Attempt to be submitted: $it")
        } ?: return
        attemptRepository.finishAttempt(attempt.attemptId)
    }
}