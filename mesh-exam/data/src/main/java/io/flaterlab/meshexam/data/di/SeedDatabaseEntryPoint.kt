package io.flaterlab.meshexam.data.di

import dagger.hilt.EntryPoint
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import io.flaterlab.meshexam.data.database.MeshDatabase
import io.flaterlab.meshexam.data.datastore.dao.UserProfileDao

@EntryPoint
@InstallIn(SingletonComponent::class)
internal interface SeedDatabaseEntryPoint {

    val userProfileDao: UserProfileDao

    val meshDatabase: MeshDatabase
}