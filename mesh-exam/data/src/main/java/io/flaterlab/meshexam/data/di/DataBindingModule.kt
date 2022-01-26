package io.flaterlab.meshexam.data.di

import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import io.flaterlab.meshexam.data.datasource.AdvertisingDataSourceImpl
import io.flaterlab.meshexam.data.datasource.DiscoveryDataSourceImpl
import io.flaterlab.meshexam.domain.api.datasource.AdvertisingDataSource
import io.flaterlab.meshexam.domain.api.datasource.DiscoveryDataSource

@Module
@InstallIn(SingletonComponent::class)
internal interface DataBindingModule {

    @Binds
    fun bindDiscoveryDataSource(impl: DiscoveryDataSourceImpl): DiscoveryDataSource

    @Binds
    fun bindAdvertisingDataSource(impl: AdvertisingDataSourceImpl): AdvertisingDataSource
}