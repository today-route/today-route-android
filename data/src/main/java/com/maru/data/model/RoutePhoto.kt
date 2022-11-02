package com.maru.data.model

import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class RoutePhoto(
    val id: Int,
    val url: Int, // TODO: 서버와 연결했을 때 String으로 변경하기
    val routeId: Int
)
