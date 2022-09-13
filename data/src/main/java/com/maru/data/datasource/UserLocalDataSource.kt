package com.maru.data.datasource

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.intPreferencesKey
import com.maru.data.datasource.UserLocalDataSource.PreferencesKeys.USER_ID
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class UserLocalDataSource @Inject constructor(
    private val dataStore: DataStore<Preferences>
) : UserDataSource.Local {

    override suspend fun saveSignInUserId(id: Int) {
        dataStore.edit { prefs ->
            prefs[USER_ID] = id
        }
    }

    override suspend fun getSignedInUserId(): Flow<Int> =
        dataStore.data.map { prefs ->
            prefs[USER_ID] ?: -1
        }

    companion object PreferencesKeys {
        val USER_ID = intPreferencesKey("user_id")
    }
}