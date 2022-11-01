package com.maru.data.datasource.route

import com.maru.data.model.Route

interface RouteDataSource {

    suspend fun getAllRoute(coupleId: Int): Result<List<Route>>
}