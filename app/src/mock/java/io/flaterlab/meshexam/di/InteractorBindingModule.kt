package io.flaterlab.meshexam.di

import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent
import io.flaterlab.meshexam.domain.impl.mock.MockExaminationInteractor
import io.flaterlab.meshexam.domain.interactor.ExaminationInteractor

@Module
@InstallIn(ViewModelComponent::class)
interface InteractorBindingModule {

    @Binds
    fun bindExaminationInteractor(impl: MockExaminationInteractor): ExaminationInteractor
}