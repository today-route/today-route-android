package com.maru.data.datasource.token

import com.maru.data.network.Token
import kotlinx.coroutines.flow.Flow

interface TokenDataSource {

    interface Local {
        suspend fun getAccessToken(): Flow<String>
        suspend fun getRefreshToken(): Flow<String>
        suspend fun saveTokens(token: Token)
        suspend fun removeTokens()
    }

    interface Remote {
        suspend fun refresh(refreshToken: String): Token
    }
}