package com.maru.data.model

import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class RoutePhoto(
    val id: Int,
    val url: String,
    val routeId: Int
)
