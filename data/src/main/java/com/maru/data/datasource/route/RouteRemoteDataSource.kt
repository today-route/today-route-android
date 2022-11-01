package com.maru.data.datasource.route

import com.maru.data.model.Route
import com.maru.data.network.server.RetrofitService
import javax.inject.Inject

class RouteRemoteDataSource @Inject constructor(
    private val retrofitService: RetrofitService
) : RouteDataSource {

    override suspend fun getAllRoute(coupleId: Int): Result<List<Route>> {
        TODO("Not yet implemented")
    }
}