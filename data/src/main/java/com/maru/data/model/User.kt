package com.maru.data.model

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class User(
    val id: Int,
    val email: String,
    val nickname: String,
    val introduction: String,
    @field:Json(name = "profile_url")
    val profileUrl: String,
)
