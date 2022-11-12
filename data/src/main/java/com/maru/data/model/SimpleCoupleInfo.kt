package com.maru.data.model

import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class SimpleCoupleInfo(val id: Int, val startDate: String, val isEnd: Boolean) {
}