package com.maru.data.datasource.token

import com.maru.data.network.RefreshRequest
import com.maru.data.network.Token
import com.maru.data.network.server.RetrofitService
import javax.inject.Inject

class TokenRemoteDataSource  @Inject constructor(
    private val retrofitService: RetrofitService,
) : TokenDataSource.Remote {

    override suspend fun refresh(refreshToken: String): Token =
        retrofitService.refresh(RefreshRequest(refreshToken))
}