package com.maru.data.model

import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class User(
    val id: Int = -1,
    val key: String = "",
    val code: String = "",
    val email: String = "",
    val gender: Gender = Gender.M,
    val nickname: String = "",
    val profileUrl: String = "",
    val birthday: String = "",
    val createdAt: String = "",
    val deletedAt: String? = null
)

enum class Gender {
    M, F
}
