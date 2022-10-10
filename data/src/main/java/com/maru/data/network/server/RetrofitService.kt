package com.maru.data.network.server

import com.maru.data.model.User
import com.maru.data.network.RegisterUserRequest
import retrofit2.http.Body
import retrofit2.http.POST

interface RetrofitService {

    @POST("user/register/")
    suspend fun registerNewUser(@Body user: RegisterUserRequest): User
}