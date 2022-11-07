package com.maru.data.repository

import com.maru.data.datasource.route.RouteDataSource
import com.maru.data.datasource.route.RouteRemoteDataSource
import com.maru.data.model.Route
import com.maru.data.model.SimpleRoute
import javax.inject.Inject

class RouteRepository @Inject constructor(
    private val remoteDataSource: RouteRemoteDataSource
) : RouteDataSource {
    override suspend fun getRouteOfMonth(year: Int, month: Int): Result<List<SimpleRoute>> =
        remoteDataSource.getRouteOfMonth(year, month)

    override suspend fun getRouteById(routeId: Int): Result<Route> =
        remoteDataSource.getRouteById(routeId)

    override suspend fun saveNewRoute(
        date: String,
        zoomLevel: Double,
        title: String,
        contents: String,
        location: String,
        filePathList: List<String>,
        geoCoordList: List<List<Double>>
    ): Result<Unit> =
        remoteDataSource.saveNewRoute(date, zoomLevel, title, contents, location, filePathList, geoCoordList)
}