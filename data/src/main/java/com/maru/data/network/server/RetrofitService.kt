package com.maru.data.network.server

import com.maru.data.network.SignUpRequest
import com.maru.data.network.SignUpResponse
import retrofit2.http.Body
import retrofit2.http.POST

interface RetrofitService {

    @POST("user/register/")
    suspend fun registerNewUser(@Body user: SignUpRequest): SignUpResponse
}