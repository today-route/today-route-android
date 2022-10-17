package com.maru.data.network.server

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import com.maru.data.datasource.token.TokenLocalDataSource
import com.maru.data.util.Constants.EMPTY_STRING
import kotlinx.coroutines.flow.map
import okhttp3.Interceptor
import okhttp3.Response
import javax.inject.Inject

class HeaderInterceptor(
    private val dataStore: DataStore<Preferences>
) :
    Interceptor {

    override fun intercept(chain: Interceptor.Chain): Response {
        val accessToken = dataStore.data.map { prefs ->
            prefs[TokenLocalDataSource.ACCESS_TOKEN] ?: EMPTY_STRING
        }
        val token = "Bearer $accessToken"
        val newRequest = chain.request().newBuilder()
            .addHeader("Authorization", token)
            .build()
        return chain.proceed(newRequest)
    }
}