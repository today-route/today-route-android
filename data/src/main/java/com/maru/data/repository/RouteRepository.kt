package com.maru.data.repository

import com.maru.data.datasource.route.RouteDataSource
import com.maru.data.datasource.route.RouteRemoteDataSource
import com.maru.data.model.Route
import com.maru.data.network.response.RouteOfMonthResponse
import javax.inject.Inject

class RouteRepository @Inject constructor(
    private val remoteDataSource: RouteRemoteDataSource
) : RouteDataSource {
    override suspend fun getRouteOfMonth(year: Int, month: Int): Result<RouteOfMonthResponse> =
        remoteDataSource.getRouteOfMonth(year, month)

    override suspend fun getRouteById(routeId: Int): Result<Route> =
        remoteDataSource.getRouteById(routeId)
}