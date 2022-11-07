package com.maru.data.datasource.route

import com.maru.data.model.Route
import com.maru.data.model.SimpleRoute

interface RouteDataSource {

    suspend fun getRouteOfMonth(year: Int, month: Int): Result<List<SimpleRoute>>
    suspend fun getRouteById(routeId: Int): Result<Route>
    suspend fun saveNewRoute(
        date: String,
        zoomLevel: Double,
        title: String,
        contents: String,
        location: String,
        filePathList: List<String>,
        geoCoordList: List<List<Double>>
    ): Result<Unit>
}