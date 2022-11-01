package com.maru.data.repository

import com.maru.data.datasource.route.RouteDataSource
import com.maru.data.datasource.route.RouteRemoteDataSource
import com.maru.data.model.Route
import javax.inject.Inject

class RouteRepository @Inject constructor(
    private val remoteDataSource: RouteRemoteDataSource
) : RouteDataSource {
    override suspend fun getAllRoute(coupleId: Int): Result<List<Route>> =
        remoteDataSource.getAllRoute(coupleId)
}