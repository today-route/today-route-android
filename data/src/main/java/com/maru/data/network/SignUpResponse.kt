package com.maru.data.network

import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class SignUpResponse(val access: String, val refresh: String) {
}