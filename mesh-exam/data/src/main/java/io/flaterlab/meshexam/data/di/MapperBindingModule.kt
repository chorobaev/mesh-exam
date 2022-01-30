package io.flaterlab.meshexam.data.di

import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import io.flaterlab.meshexam.core.Mapper
import io.flaterlab.meshexam.data.database.entity.ExamEntity
import io.flaterlab.meshexam.data.database.entity.QuestionEntity
import io.flaterlab.meshexam.data.mapper.CreateExamModelToEntityMapper
import io.flaterlab.meshexam.data.mapper.CreateQuestionModelToEntityMapper
import io.flaterlab.meshexam.data.mapper.ExamEntityToModelMapper
import io.flaterlab.meshexam.domain.api.model.CreateExamModel
import io.flaterlab.meshexam.domain.api.model.ExamModel
import io.flaterlab.meshexam.domain.model.CreateQuestionModel

@Module
@InstallIn(SingletonComponent::class)
internal interface MapperBindingModule {

    @Binds
    fun bindCreateExamModelToEntityMapper(impl: CreateExamModelToEntityMapper): Mapper<CreateExamModel, ExamEntity>

    @Binds
    fun bindExamEntityToModelMapper(impl: ExamEntityToModelMapper): Mapper<ExamEntity, ExamModel>

    @Binds
    fun bindCreateQuestionModelToEntityMapper(impl: CreateQuestionModelToEntityMapper): Mapper<CreateQuestionModel, QuestionEntity>
}