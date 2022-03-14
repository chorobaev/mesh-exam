package io.flaterlab.meshexam.data.database.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import io.flaterlab.meshexam.data.database.entity.HostingEntity

@Dao
internal interface HostingDao {

    @Insert(onConflict = OnConflictStrategy.ABORT)
    suspend fun insert(vararg hostings: HostingEntity)
}