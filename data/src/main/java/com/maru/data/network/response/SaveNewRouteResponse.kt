package com.maru.data.network.response

import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
class SaveNewRouteResponse(val id: Int) {
}