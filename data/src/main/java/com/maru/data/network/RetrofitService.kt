package com.maru.data.network

import com.maru.data.model.User
import retrofit2.http.Body
import retrofit2.http.POST

interface RetrofitService {

    @POST("user/register/")
    suspend fun postNewUser(@Body user: User): User
}