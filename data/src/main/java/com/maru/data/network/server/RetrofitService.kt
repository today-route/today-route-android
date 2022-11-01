package com.maru.data.network.server

import com.maru.data.model.CoupleInfo
import com.maru.data.model.User
import com.maru.data.network.*
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST

interface RetrofitService {

    @POST("user/signup")
    suspend fun registerNewUser(@Body user: SignUpRequest): SignUpResponse

    @POST("user/signin")
    suspend fun signInUser(@Body key: SignInRequest): Token

    @POST("user/refresh")
    suspend fun refresh(@Body refresh: RefreshRequest): Token

    @POST("couple")
    suspend fun createCouple(@Body coupleRequest: CreateCoupleRequest)

    @GET("couple")
    suspend fun getMyCoupleData(): CoupleInfo

    @GET("user")
    suspend fun getMyUserData(): User
}