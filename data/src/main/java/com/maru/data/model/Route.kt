package com.maru.data.model

import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class Route(
    val id: Int,
    val userId: Int,
    val date: String,
    val zoomLevel: Int,
    val title: String,
    val content: String,
    val location: String,
    val photoList: List<String> = listOf(),
    val geoCoordList: List<List<Double>> = listOf()
)