package io.flaterlab.meshexam.data.communication.fromClient

import com.google.gson.Gson
import io.flaterlab.meshexam.data.communication.PayloadHandler
import io.flaterlab.meshexam.data.database.MeshDatabase
import io.flaterlab.meshexam.data.database.entity.host.ExamEventEntity
import io.flaterlab.meshexam.data.strategy.IdGeneratorStrategy
import io.flaterlab.meshexam.librariy.mesh.common.dto.FromClientPayload
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.util.*
import javax.inject.Inject

internal class ExamEventPayloadHandler @Inject constructor(
    private val gson: Gson,
    private val idGenerator: IdGeneratorStrategy,
    database: MeshDatabase,
) : PayloadHandler.Handler<FromClientPayload> {

    private val examEventDao = database.examEventDao()

    override suspend fun handle(payload: FromClientPayload): Boolean {
        if (payload.contentType != ExamEventDto.contentType) return false
        val event = gson.fromJson(payload.data, ExamEventDto::class.java)
        saveEvent(event)
        return true
    }

    private suspend fun saveEvent(event: ExamEventDto) {
        val entity = ExamEventEntity(
            eventId = idGenerator.generate(),
            hostingId = event.hostingId,
            authorClientId = event.clientId,
            eventType = event.eventType.typeInt,
            createdAt = Date().time,
        )
        withContext(Dispatchers.IO) {
            examEventDao.insert(entity)
        }
    }
}