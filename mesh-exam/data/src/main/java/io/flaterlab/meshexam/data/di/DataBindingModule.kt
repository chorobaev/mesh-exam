package io.flaterlab.meshexam.data.di

import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import io.flaterlab.meshexam.data.datasource.RemoteDataSourceImpl
import io.flaterlab.meshexam.domain.api.datasource.RemoteDataSource

@Module
@InstallIn(SingletonComponent::class)
internal interface DataBindingModule {

    @Binds
    fun bindRemoteDataSource(impl: RemoteDataSourceImpl): RemoteDataSource
}