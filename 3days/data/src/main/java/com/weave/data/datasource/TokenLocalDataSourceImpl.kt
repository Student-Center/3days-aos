package com.weave.data.datasource

import android.util.Log
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import com.weave.model.auth.AuthToken
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class TokenLocalDataSourceImpl @Inject constructor(
    private val dataStore: DataStore<Preferences>
) : TokenLocalDataSource {

    private object PreferencesKeys {
        val ACCESS_TOKEN = stringPreferencesKey("access_token")
        val REFRESH_TOKEN = stringPreferencesKey("refresh_token")
    }

    override suspend fun saveTokens(accessToken: String, refreshToken: String): Result<Unit> {
        Log.e("TEST", "SAVE: $accessToken, $refreshToken")

        return try {
            dataStore.edit { preferences ->
                preferences[PreferencesKeys.ACCESS_TOKEN] = accessToken
                preferences[PreferencesKeys.REFRESH_TOKEN] = refreshToken
            }
            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override fun getTokens(): Flow<AuthToken> {
        Log.e("TEST", "GET")

        return dataStore.data.map { preferences ->
            AuthToken(
                accessToken = preferences[PreferencesKeys.ACCESS_TOKEN] ?: "",
                refreshToken = preferences[PreferencesKeys.REFRESH_TOKEN] ?: "",
            )
        }
    }

    override suspend fun clearTokens(): Result<Unit> {
        Log.e("TEST", "Clear")

        return try {
            dataStore.edit { preferences ->
                preferences.clear()
            }
            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}