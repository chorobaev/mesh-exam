package io.flaterlab.meshexam.data.di

import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import io.flaterlab.meshexam.data.datasource.AdvertisingDataSourceImpl
import io.flaterlab.meshexam.data.datasource.DiscoveryDataSourceImpl
import io.flaterlab.meshexam.data.datasource.ExamDataSourceImpl
import io.flaterlab.meshexam.data.datasource.MeshDataSourceImpl
import io.flaterlab.meshexam.domain.api.datasource.AdvertisingDataSource
import io.flaterlab.meshexam.domain.datasource.DiscoveryDataSource
import io.flaterlab.meshexam.domain.datasource.ExamDataSource
import io.flaterlab.meshexam.domain.datasource.MeshDataSource

@Module
@InstallIn(SingletonComponent::class)
internal interface DataSourceBindingModule {

    @Binds
    fun bindDiscoveryDataSource(impl: DiscoveryDataSourceImpl): DiscoveryDataSource

    @Binds
    fun bindAdvertisingDataSource(impl: AdvertisingDataSourceImpl): AdvertisingDataSource

    @Binds
    fun bindExamDataSource(impl: ExamDataSourceImpl): ExamDataSource

    @Binds
    fun bindMeshDataSource(impl: MeshDataSourceImpl): MeshDataSource
}