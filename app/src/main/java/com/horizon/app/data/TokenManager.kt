package com.horizon.app.data

import android.content.Context
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map

private val Context.dataStore by preferencesDataStore(name = "horizon_session")

class TokenManager(private val context: Context) {

    companion object {
        private val TOKEN_KEY = stringPreferencesKey("auth_token")
        private val USER_ID_KEY = stringPreferencesKey("user_id")
        private val USER_NAME_KEY = stringPreferencesKey("user_name")
    }

    val tokenFlow: Flow<String?> = context.dataStore.data.map { it[TOKEN_KEY] }

    suspend fun getToken(): String? = context.dataStore.data.first()[TOKEN_KEY]

    suspend fun saveSession(token: String, userId: Int, userName: String) {
        context.dataStore.edit { prefs ->
            prefs[TOKEN_KEY] = token
            prefs[USER_ID_KEY] = userId.toString()
            prefs[USER_NAME_KEY] = userName
        }
    }

    suspend fun getUserId(): Int? = context.dataStore.data.first()[USER_ID_KEY]?.toIntOrNull()

    suspend fun clearSession() {
        context.dataStore.edit { it.clear() }
    }

    suspend fun getAuthHeader(): String? {
        val token = getToken() ?: return null
        return "Bearer $token"
    }
}
