package io.flaterlab.meshexam.data.di

import android.content.Context
import androidx.room.Room
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
    ): MeshDatabase = Room
        .databaseBuilder(context, MeshDatabase::class.java, "mesh_db")
        .build()
}


