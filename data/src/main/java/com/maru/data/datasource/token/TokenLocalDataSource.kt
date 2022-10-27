package com.maru.data.datasource.token

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import com.maru.data.network.Token
import com.maru.data.util.Constants.EMPTY_STRING
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject


class TokenLocalDataSource @Inject constructor(
    private val dataStore: DataStore<Preferences>
) : TokenDataSource.Local {

    override suspend fun getAccessToken(): Flow<String>  =
        dataStore.data.map { prefs ->
            prefs[ACCESS_TOKEN] ?: EMPTY_STRING
        }


    override suspend fun getRefreshToken(): Flow<String> =
        dataStore.data.map { prefs ->
            prefs[REFRESH_TOKEN] ?: EMPTY_STRING
        }

    override suspend fun saveTokens(token: Token) {
        dataStore.edit { prefs ->
            prefs[ACCESS_TOKEN] = token.access
            prefs[REFRESH_TOKEN] = token.refresh
        }
    }

    override suspend fun removeTokens() {
        dataStore.edit { prefs ->
            prefs.remove(ACCESS_TOKEN)
            prefs.remove(REFRESH_TOKEN)
        }
    }

    companion object PreferencesKeys {
        val ACCESS_TOKEN = stringPreferencesKey("access_token")
        val REFRESH_TOKEN = stringPreferencesKey("refresh_token")
    }
}