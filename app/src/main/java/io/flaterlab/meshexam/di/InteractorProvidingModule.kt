package io.flaterlab.meshexam.di

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import io.flaterlab.meshexam.BuildConfig
import io.flaterlab.meshexam.domain.impl.ContentInteractorImpl
import io.flaterlab.meshexam.domain.impl.ExaminationInteractorImpl
import io.flaterlab.meshexam.domain.impl.MeshInteractorImpl
import io.flaterlab.meshexam.domain.impl.ProfileInteractorImpl
import io.flaterlab.meshexam.domain.impl.mock.MockExaminationInteractor
import io.flaterlab.meshexam.domain.impl.mock.MockMeshInteractor
import io.flaterlab.meshexam.domain.interactor.ContentInteractor
import io.flaterlab.meshexam.domain.interactor.ExaminationInteractor
import io.flaterlab.meshexam.domain.interactor.MeshInteractor
import io.flaterlab.meshexam.domain.interactor.ProfileInteractor

@Module
@InstallIn(SingletonComponent::class)
class InteractorProvidingModule {

    @Provides
    fun provideExaminationInteractor(
        impl: ExaminationInteractorImpl,
        mock: MockExaminationInteractor,
    ): ExaminationInteractor = when (BuildConfig.BUILD_TYPE) {
        BUILD_TYPE_MOCK -> mock
        else -> impl
    }

    @Provides
    fun provideProfileInteractor(impl: ProfileInteractorImpl): ProfileInteractor = impl

    @Provides
    fun provideMeshInteractor(
        impl: MeshInteractorImpl,
        mock: MockMeshInteractor,
    ): MeshInteractor = when (BuildConfig.BUILD_TYPE) {
        BUILD_TYPE_MOCK -> mock
        else -> impl
    }

    @Provides
    fun provideContentInteractor(impl: ContentInteractorImpl): ContentInteractor = impl

    companion object {
        const val BUILD_TYPE_MOCK = "mock"
    }
}