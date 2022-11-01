package com.maru.data.network

import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class RefreshRequest(val refresh: String) {
}