package com.maru.data.model

import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
class RouteDiary(val route: Route, val routePhotos: List<RoutePhoto>, val geoCoords: List<GeoCoord>) {
}