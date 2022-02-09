package io.flaterlab.meshexam.data.di

import android.content.Context
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import io.flaterlab.meshexam.data.database.MeshDatabase
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
internal class RoomModule {

    @Provides
    @Singleton
    fun provideMeshDatabase(
        @ApplicationContext context: Context,
    ): MeshDatabase = MeshDatabase.getInstance(context)
}


