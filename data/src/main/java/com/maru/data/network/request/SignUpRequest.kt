package com.maru.data.network.request

import com.maru.data.model.Gender
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class SignUpRequest(
    val key: String,
    val gender: Gender,
    val email: String,
    val nickname: String,
    val profileUrl: String,
    val birthday: String
)