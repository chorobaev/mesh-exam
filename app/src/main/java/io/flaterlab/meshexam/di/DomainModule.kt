package io.flaterlab.meshexam.di

import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent
import io.flaterlab.meshexam.domain.api.usecase.AdvertiseExamUseCase
import io.flaterlab.meshexam.domain.api.usecase.CreateExamUseCase
import io.flaterlab.meshexam.domain.api.usecase.DiscoverExamsUseCase
import io.flaterlab.meshexam.domain.api.usecase.GetMyExamUseCase
import io.flaterlab.meshexam.domain.impl.AdvertiseExamUseCaseImpl
import io.flaterlab.meshexam.domain.impl.CreateExamUseCaseImpl
import io.flaterlab.meshexam.domain.impl.DiscoverExamUseCaseImpl
import io.flaterlab.meshexam.domain.impl.GetMyExamsUseCaseImpl

@Module
@InstallIn(ViewModelComponent::class)
interface DomainModule {

    @Binds
    fun bindDiscoverExamUseCase(impl: DiscoverExamUseCaseImpl): DiscoverExamsUseCase

    @Binds
    fun bindAdvertiseExamUseCase(impl: AdvertiseExamUseCaseImpl): AdvertiseExamUseCase

    @Binds
    fun bindCreateTestUseCase(impl: CreateExamUseCaseImpl): CreateExamUseCase

    @Binds
    fun bindGetMyExamsUseCase(impl: GetMyExamsUseCaseImpl): GetMyExamUseCase
}