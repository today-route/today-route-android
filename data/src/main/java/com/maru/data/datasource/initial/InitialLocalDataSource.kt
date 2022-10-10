package com.maru.data.datasource.initial

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.intPreferencesKey
import com.maru.data.datasource.initial.InitialLocalDataSource.PreferencesKeys.USER_ID
import com.maru.data.model.CoupleInfo
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class InitialLocalDataSource @Inject constructor(
    private val dataStore: DataStore<Preferences>
) : InitialDataSource.Local {

    override suspend fun saveSignInUserId(id: Int) {
        dataStore.edit { prefs ->
            prefs[USER_ID] = id
        }
    }

    override suspend fun getSignedInUserId(): Flow<Int> =
        dataStore.data.map { prefs ->
            prefs[USER_ID] ?: -1
        }

    override suspend fun saveCoupleId(coupleId: Int) {
        dataStore.edit { prefs ->
            prefs[COUPLE_ID] = coupleId
        }
    }

    override suspend fun getCoupleId(): Flow<Int> =
        dataStore.data.map { prefs ->
            prefs[COUPLE_ID] ?: -1
        }

    companion object PreferencesKeys {
        val USER_ID = intPreferencesKey("user_id")
        val COUPLE_ID = intPreferencesKey("couple_id")
    }
}