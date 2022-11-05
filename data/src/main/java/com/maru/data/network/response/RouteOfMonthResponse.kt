package com.maru.data.network.response

import com.maru.data.model.SimpleRoute
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class RouteOfMonthResponse(val simpleRouteList: List<SimpleRoute>)