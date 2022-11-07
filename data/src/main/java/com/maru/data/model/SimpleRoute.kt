package com.maru.data.model

import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
class SimpleRoute(
    val id: Int,
    val date: String,
    val zoomLevel: Double,
    val title: String,
    val content: String,
    val location: String
)