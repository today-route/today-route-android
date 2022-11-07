package com.maru.data.model

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class Route(
    @Json(name = "content")
    val content: String,
    @Json(name = "date")
    val date: String,
    @Json(name = "geoCoord")
    val geoCoord: List<List<Double>>,
    @Json(name = "id")
    val id: Int,
    @Json(name = "location")
    val location: String,
    @Json(name = "routePhoto")
    val routePhoto: List<RoutePhoto>,
    @Json(name = "title")
    val title: String,
    @Json(name = "zoomLevel")
    val zoomLevel: Double
)