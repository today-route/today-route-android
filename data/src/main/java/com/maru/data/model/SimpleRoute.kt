package com.maru.data.model

import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
class SimpleRoute(val id: Int, val date: String, val title: String, val location: String)