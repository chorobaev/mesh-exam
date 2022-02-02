package io.flaterlab.meshexam.data.di

import android.content.Context
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import io.flaterlab.meshexam.data.strategy.IdGeneratorStrategy
import io.flaterlab.meshexam.librariy.mesh.host.HostMeshManager

@Module
@InstallIn(SingletonComponent::class)
internal class DataModule {

    @Provides
    fun provideGson(): Gson =
        GsonBuilder()
            .excludeFieldsWithoutExposeAnnotation()
            .create()

    @Provides
    fun provideIdGeneratorStrategy(): IdGeneratorStrategy = IdGeneratorStrategy()

    @Provides
    fun provideHostMeshManager(
        @ApplicationContext context: Context,
    ): HostMeshManager = HostMeshManager
        .getInstance(context)
}