package com.maru.data.network.server

import com.maru.data.model.CoupleInfo
import com.maru.data.model.User
import com.maru.data.network.SignInRequest
import com.maru.data.network.SignUpRequest
import com.maru.data.network.SignUpResponse
import com.maru.data.network.Token
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Headers
import retrofit2.http.POST

interface RetrofitService {

    @POST("user/signup")
    suspend fun registerNewUser(@Body user: SignUpRequest): SignUpResponse

    @POST("user/signin")
    suspend fun signInUser(@Body key: SignInRequest): Token

    @POST("user/refresh/")
    suspend fun refresh(@Body refresh: String): Token

    @POST("couple/")
    suspend fun createCouple(@Body code: String, @Body startDate: String): CoupleInfo

    @GET("couple/")
    suspend fun getMyCoupleData(): CoupleInfo

    @GET("user")
    suspend fun getMyUserData(): User
}