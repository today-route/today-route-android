package com.maru.data.network

import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class SignInRequest(val key: String) {
}