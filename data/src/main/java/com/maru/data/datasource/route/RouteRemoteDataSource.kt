package com.maru.data.datasource.route

import com.maru.data.model.Route
import com.maru.data.network.response.RouteOfMonthResponse
import com.maru.data.network.server.RetrofitService
import javax.inject.Inject

class RouteRemoteDataSource @Inject constructor(
    private val retrofitService: RetrofitService
) : RouteDataSource {

    override suspend fun getRouteOfMonth(year: Int, month: Int): Result<RouteOfMonthResponse> = runCatching {
        retrofitService.getRouteOfMonth(year.toString(), month.toString())
    }

    override suspend fun getRouteById(routeId: Int): Result<Route> = runCatching {
        retrofitService.getRouteById(routeId)
    }
}