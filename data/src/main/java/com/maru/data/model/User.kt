package com.maru.data.model

import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class User(
    val key: String,
    val email: String,
    val nickname: String,
    val introduction: String,
    val profileUrl: String,
)
