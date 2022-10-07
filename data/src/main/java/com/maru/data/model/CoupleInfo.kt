package com.maru.data.model

import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class CoupleInfo(
    val id: Int = -1,
    val startDate: String = "",
    val isEnd: Boolean = false,
    val user1Id: Int = -1,
    val user2Id: Int = -1
)
