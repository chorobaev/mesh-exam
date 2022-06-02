package io.flaterlab.meshexam.data.di

import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import io.flaterlab.meshexam.data.datasource.ResultReceiverDataSourceImpl
import io.flaterlab.meshexam.data.datasource.ResultSenderDataSourceImpl
import io.flaterlab.meshexam.domain.datasource.ResultReceiverDataSource
import io.flaterlab.meshexam.domain.datasource.ResultSenderDataSource

@Module
@InstallIn(SingletonComponent::class)
internal interface DataSourceBindingModule {

    @Binds
    fun bindResultReceiverDataSource(impl: ResultReceiverDataSourceImpl): ResultReceiverDataSource

    @Binds
    fun bindResultSenderDataSource(impl: ResultSenderDataSourceImpl): ResultSenderDataSource
}