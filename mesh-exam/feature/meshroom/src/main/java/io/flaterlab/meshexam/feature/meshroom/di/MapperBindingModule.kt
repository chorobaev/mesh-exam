package io.flaterlab.meshexam.feature.meshroom.di

import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent
import io.flaterlab.meshexam.core.Mapper
import io.flaterlab.meshexam.domain.exam.model.ExamEventModel
import io.flaterlab.meshexam.feature.meshroom.dvo.EventDvo
import io.flaterlab.meshexam.feature.meshroom.ui.event.ExamEventModelToDvoMapper

@Module
@InstallIn(ViewModelComponent::class)
internal interface MapperBindingModule {

    @Binds
    fun bindExamEventMapper(imp: ExamEventModelToDvoMapper): Mapper<ExamEventModel, EventDvo>
}