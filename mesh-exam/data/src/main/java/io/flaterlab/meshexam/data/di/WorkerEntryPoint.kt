package io.flaterlab.meshexam.data.di

import dagger.hilt.EntryPoint
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import io.flaterlab.meshexam.data.database.MeshDatabase
import io.flaterlab.meshexam.data.datastore.dao.UserProfileDao
import io.flaterlab.meshexam.domain.repository.AttemptRepository
import io.flaterlab.meshexam.domain.repository.MeshRepository

@EntryPoint
@InstallIn(SingletonComponent::class)
internal interface WorkerEntryPoint {

    val userProfileDao: UserProfileDao

    val meshDatabase: MeshDatabase

    val attemptRepository: AttemptRepository

    val meshRepository: MeshRepository
}