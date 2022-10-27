package com.maru.data.repository

import com.maru.data.datasource.token.TokenDataSource
import com.maru.data.datasource.token.TokenLocalDataSource
import com.maru.data.datasource.token.TokenRemoteDataSource
import com.maru.data.network.Token
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class TokenRepository @Inject constructor(
    private val tokenLocalDataSource: TokenLocalDataSource,
    private val tokenRemoteDataSource: TokenRemoteDataSource
) : TokenDataSource, TokenDataSource.Local, TokenDataSource.Remote {

    override suspend fun getAccessToken(): Flow<String> = tokenLocalDataSource.getAccessToken()

    override suspend fun getRefreshToken(): Flow<String> =
        tokenLocalDataSource.getRefreshToken()

    override suspend fun saveTokens(token: Token) {
        tokenLocalDataSource.saveTokens(token)
    }

    override suspend fun removeTokens() {
        tokenLocalDataSource.removeTokens()
    }

    override suspend fun refresh(refreshToken: String): Token =
        tokenRemoteDataSource.refresh(refreshToken)
}