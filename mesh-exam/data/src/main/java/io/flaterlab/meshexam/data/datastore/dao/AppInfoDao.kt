package io.flaterlab.meshexam.data.datastore.dao

import android.content.Context
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.preferencesDataStore
import dagger.hilt.android.qualifiers.ApplicationContext
import io.flaterlab.meshexam.data.datastore.entity.AppInfoEntity
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
internal class AppInfoDao @Inject constructor(
    @ApplicationContext context: Context,
) {

    private val Context.appInfoDataStore by preferencesDataStore(APP_INFO_PREFERENCES)
    private val appInfoPrefs = context.appInfoDataStore

    suspend fun getAppInfo(): AppInfoEntity {
        return appInfoPrefs.data
            .map {
                val prefs = appInfoPrefs.edit { mutablePrefs ->
                    if (mutablePrefs[IS_FIRST_STARTUP] == null) {
                        mutablePrefs[IS_FIRST_STARTUP] = true
                    }
                }
                AppInfoEntity(
                    isFirstStartUp = prefs[IS_FIRST_STARTUP]!!,
                )
            }
            .first()
    }

    suspend fun updateAppInfo(info: AppInfoEntity) {
        appInfoPrefs.edit { mutablePrefs ->
            mutablePrefs[IS_FIRST_STARTUP] = info.isFirstStartUp
        }
    }

    companion object {
        private const val APP_INFO_PREFERENCES = "APP_INFO_PREFERENCES"
        private val IS_FIRST_STARTUP = booleanPreferencesKey("FIRST_STARTUP")
    }
}