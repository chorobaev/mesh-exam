package io.flaterlab.meshexam.data.di

import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import io.flaterlab.meshexam.core.Mapper
import io.flaterlab.meshexam.data.communication.Message
import io.flaterlab.meshexam.data.communication.fromClient.MessageToFromClientPayloadMapper
import io.flaterlab.meshexam.data.communication.fromHost.MessageToFromHostPayloadMapper
import io.flaterlab.meshexam.data.database.entity.ExamEntity
import io.flaterlab.meshexam.data.database.entity.QuestionEntity
import io.flaterlab.meshexam.data.datastore.entity.UserProfileEntity
import io.flaterlab.meshexam.data.mapper.CreateQuestionModelToEntityMapper
import io.flaterlab.meshexam.data.mapper.ExamEntityToModelMapper
import io.flaterlab.meshexam.data.mapper.UserProfileEntityToModelMapper
import io.flaterlab.meshexam.domain.create.model.CreateQuestionModel
import io.flaterlab.meshexam.domain.create.model.ExamModel
import io.flaterlab.meshexam.domain.profile.model.UserProfileModel
import io.flaterlab.meshexam.librariy.mesh.common.dto.FromClientPayload
import io.flaterlab.meshexam.librariy.mesh.common.dto.FromHostPayload

@Module
@InstallIn(SingletonComponent::class)
internal interface MapperBindingModule {

    @Binds
    fun bindExamEntityToModelMapper(impl: ExamEntityToModelMapper): Mapper<ExamEntity, ExamModel>

    @Binds
    fun bindCreateQuestionModelToEntityMapper(impl: CreateQuestionModelToEntityMapper): Mapper<CreateQuestionModel, QuestionEntity>

    @Binds
    fun bindUserProfileEntityToModelMapper(impl: UserProfileEntityToModelMapper): Mapper<UserProfileEntity, UserProfileModel>

    @Binds
    fun bindMessageToFromHostPayloadMapper(impl: MessageToFromHostPayloadMapper): Mapper<Message, FromHostPayload>

    @Binds
    fun bindMessageToFromClientPayloadMapper(impl: MessageToFromClientPayloadMapper): Mapper<Message, FromClientPayload>
}