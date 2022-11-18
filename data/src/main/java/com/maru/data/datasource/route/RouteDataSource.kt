package com.maru.data.datasource.route

import com.maru.data.model.Route
import com.maru.data.model.SimpleRoute
import com.maru.data.network.response.EditRouteResponse
import com.maru.data.network.response.SaveNewRouteResponse
import java.io.File

interface RouteDataSource {

    suspend fun getRouteOfMonth(year: Int, month: Int): Result<List<SimpleRoute>>
    suspend fun getRouteById(routeId: Int): Result<Route>
    suspend fun saveNewRoute(
        date: String,
        zoomLevel: Double,
        title: String,
        contents: String,
        location: String,
        fileList: List<File>,
        geoCoordList: List<List<Double>>
    ): Result<SaveNewRouteResponse>

    suspend fun editRoute(
        routeId: Int,
        title: String,
        zoomLevel: Double,
        content: String,
        location: String,
        photos: List<File>
    ): Result<EditRouteResponse>

    suspend fun deleteRoute(routeId: Int): Result<Unit>
}