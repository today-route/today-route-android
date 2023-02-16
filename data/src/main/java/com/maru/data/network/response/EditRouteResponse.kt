package com.maru.data.network.response

import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class EditRouteResponse(
    val id: Int,
    val coupleId: Int,
    val date: String,
    val zoomLevel: Double,
    val title: String,
    val content: String,
    val location: String
) {
}