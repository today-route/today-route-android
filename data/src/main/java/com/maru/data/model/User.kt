package com.maru.data.model

import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class User(
    val id: Int,
    val key: String,
    val code: String,
    val email: String,
    val gender: Gender,
    val nickname: String,
    val profileUrl: String,
    val birthday: String,
    val createdAt: String,
    val deletedAt: String?
)

enum class Gender {
    M, F
}
