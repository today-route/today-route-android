package com.maru.data.model

import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class GeoCoord(val id: Int, val routeId: Int, val latitude: Double, val longitude: Double)