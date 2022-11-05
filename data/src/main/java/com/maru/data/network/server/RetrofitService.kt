package com.maru.data.network.server

import com.maru.data.model.CoupleInfo
import com.maru.data.model.Route
import com.maru.data.model.User
import com.maru.data.network.*
import com.maru.data.network.request.CreateCoupleRequest
import com.maru.data.network.request.RefreshRequest
import com.maru.data.network.request.SignInRequest
import com.maru.data.network.request.SignUpRequest
import com.maru.data.network.response.RouteOfMonthResponse
import com.maru.data.network.response.SignUpResponse
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path
import retrofit2.http.Query

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

    @GET("route")
    suspend fun getRouteOfMonth(@Query("year") year: String, @Query("month") month: String): RouteOfMonthResponse

    @GET("route/{routeId}")
    suspend fun getRouteById(@Path("routeId") routeId: Int): Route
}