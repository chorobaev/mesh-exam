package io.flaterlab.meshexam.data.di

import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import dagger.multibindings.IntoSet
import io.flaterlab.meshexam.data.communication.PayloadHandler
import io.flaterlab.meshexam.data.communication.fromClient.AttemptPayloadHandler
import io.flaterlab.meshexam.data.communication.fromClient.ExamEventPayloadHandler
import io.flaterlab.meshexam.data.communication.fromHost.ExamContentPayloadHandler
import io.flaterlab.meshexam.data.communication.fromHost.FinishExamEventPayloadHandler
import io.flaterlab.meshexam.librariy.mesh.common.dto.FromClientPayload
import io.flaterlab.meshexam.librariy.mesh.common.dto.FromHostPayload

@Module
@InstallIn(SingletonComponent::class)
internal interface PayloadHandlerBinding {

    @Binds
    @IntoSet
    fun bindExamContentPayloadHandler(impl: ExamContentPayloadHandler): PayloadHandler.Handler<FromHostPayload>

    @Binds
    @IntoSet
    fun bindFinishExamEventPayloadHandler(impl: FinishExamEventPayloadHandler): PayloadHandler.Handler<FromHostPayload>

    @Binds
    @IntoSet
    fun bindAttemptPayloadHandler(impl: AttemptPayloadHandler): PayloadHandler.Handler<FromClientPayload>

    @Binds
    fun bindAttemptPayloadHandlerSingle(impl: AttemptPayloadHandler): PayloadHandler.Handler<FromClientPayload>

    @Binds
    @IntoSet
    fun bindExamEventPayloadHandler(impl: ExamEventPayloadHandler): PayloadHandler.Handler<FromClientPayload>
}