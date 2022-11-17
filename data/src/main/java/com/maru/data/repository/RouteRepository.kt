package com.maru.data.repository

import com.maru.data.datasource.route.RouteDataSource
import com.maru.data.datasource.route.RouteRemoteDataSource
import com.maru.data.model.Route
import com.maru.data.model.SimpleRoute
import com.maru.data.network.response.EditRouteResponse
import com.maru.data.network.response.SaveNewRouteResponse
import java.io.File
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
        fileList: List<File>,
        geoCoordList: List<List<Double>>
    ): Result<SaveNewRouteResponse> =
        remoteDataSource.saveNewRoute(date, zoomLevel, title, contents, location, fileList, geoCoordList)

    override suspend fun editRoute(
        routeId: Int,
        title: String,
        zoomLevel: Double,
        content: String,
        location: String,
        photos: List<File>
    ): Result<EditRouteResponse> =
        remoteDataSource.editRoute(routeId, title, zoomLevel, content, location, photos)
}