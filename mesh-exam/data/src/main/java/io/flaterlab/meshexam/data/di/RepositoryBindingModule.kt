package io.flaterlab.meshexam.data.di

import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import io.flaterlab.meshexam.data.repository.*
import io.flaterlab.meshexam.domain.repository.*

@Module
@InstallIn(SingletonComponent::class)
internal interface RepositoryBindingModule {

    @Binds
    fun bindDiscoveryRepository(impl: DiscoveryRepositoryImpl): DiscoveryRepository

    @Binds
    fun bindAdvertisingRepository(impl: AdvertisingRepositoryImpl): AdvertisingRepository

    @Binds
    fun bindExamRepository(impl: ExamRepositoryImpl): ExamRepository

    @Binds
    fun bindMeshRepository(impl: MeshRepositoryImpl): MeshRepository

    @Binds
    fun bindProfileRepository(impl: ProfileRepositoryImpl): ProfileRepository
}