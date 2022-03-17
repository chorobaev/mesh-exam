package io.flaterlab.meshexam.data.di

import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import dagger.multibindings.IntoSet
import io.flaterlab.meshexam.data.communication.ExamContentPayloadHandler
import io.flaterlab.meshexam.data.communication.FromHostPayloadHandler

@Module
@InstallIn(SingletonComponent::class)
internal interface PayloadHandlerBinding {

    @Binds
    @IntoSet
    fun bindExamContentPayloadHandler(impl: ExamContentPayloadHandler): FromHostPayloadHandler.Handler
}