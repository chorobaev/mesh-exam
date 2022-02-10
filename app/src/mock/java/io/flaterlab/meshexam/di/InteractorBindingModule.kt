package io.flaterlab.meshexam.di

import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import io.flaterlab.meshexam.domain.impl.ProfileInteractorImpl
import io.flaterlab.meshexam.domain.impl.mock.MockExaminationInteractor
import io.flaterlab.meshexam.domain.interactor.ExaminationInteractor
import io.flaterlab.meshexam.domain.interactor.ProfileInteractor

@Module
@InstallIn(SingletonComponent::class)
interface InteractorBindingModule {

    @Binds
    fun bindExaminationInteractor(impl: MockExaminationInteractor): ExaminationInteractor

    @Binds
    fun bindProfileInteractor(impl: ProfileInteractorImpl): ProfileInteractor
}