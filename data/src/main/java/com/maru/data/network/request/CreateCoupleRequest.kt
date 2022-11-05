package com.maru.data.network.request

import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class CreateCoupleRequest(val code: String, val startDate: String)
