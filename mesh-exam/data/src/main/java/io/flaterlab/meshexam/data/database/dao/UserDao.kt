package io.flaterlab.meshexam.data.database.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import io.flaterlab.meshexam.data.database.entity.UserEntity
import kotlinx.coroutines.flow.Flow

@Dao
internal interface UserDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(vararg users: UserEntity)

    @Query("SELECT * FROM users WHERE userId = :userId")
    suspend fun getUserById(userId: String): UserEntity

    @Query(
        """
            SELECT * FROM users WHERE userId 
            IN (SELECT userId FROM attempts WHERE hostingId = :hostingId)
        """
    )
    suspend fun getUsersByHostingId(hostingId: String): List<UserEntity>

    @Query(
        "SELECT * FROM users WHERE " +
                "userId IN (SELECT userId FROM attempts WHERE hostingId = :hostingId) AND " +
                "fullName LIKE '%' || :searchText || '%'"
    )
    fun searchUsersByHostingId(hostingId: String, searchText: String): Flow<List<UserEntity>>
}