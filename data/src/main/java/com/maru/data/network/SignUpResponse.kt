package com.maru.data.network

import com.maru.data.model.User
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class SignUpResponse(val access: String, val refresh: String, val user: User) {
}