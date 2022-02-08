package io.flaterlab.meshexam.data.datastore.dao

import android.content.Context
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import dagger.hilt.android.qualifiers.ApplicationContext
import io.flaterlab.meshexam.data.datastore.entity.UserProfileEntity
import io.flaterlab.meshexam.data.strategy.IdGeneratorStrategy
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
internal class UserProfileDao @Inject constructor(
    @ApplicationContext context: Context,
    private val idGenerator: IdGeneratorStrategy,
) {
    private val Context.dataStore by preferencesDataStore(PROFILE_PREFERENCES)
    private val preferences = context.dataStore

    fun userProfile(): Flow<UserProfileEntity> = preferences.data
        .map {
            val prefs = preferences.edit { mutablePrefs ->
                if (mutablePrefs[USER_ID] == null) {
                    mutablePrefs[USER_ID] = idGenerator.generate()
                }
            }
            UserProfileEntity(
                id = prefs[USER_ID] ?: throw IllegalStateException("User id must not be null"),
                firstName = prefs[USER_FIRST_NAME],
                lastName = prefs[USER_LAST_NAME],
                info = prefs[USER_INFO_NAME]
            )
        }

    suspend fun updateUserProfile(
        firstName: String,
        lastName: String,
        info: String,
    ) {
        preferences.edit { prefs ->
            prefs[USER_FIRST_NAME] = firstName
            prefs[USER_LAST_NAME] = lastName
            prefs[USER_INFO_NAME] = info
        }
    }

    companion object {
        private const val PROFILE_PREFERENCES = "PROFILE_PREFERENCES"
        private val USER_ID = stringPreferencesKey("USER_ID")
        private val USER_FIRST_NAME = stringPreferencesKey("USER_FIRST_NAME")
        private val USER_LAST_NAME = stringPreferencesKey("USER_LAST_NAME")
        private val USER_INFO_NAME = stringPreferencesKey("USER_INFO_NAME")
    }
}