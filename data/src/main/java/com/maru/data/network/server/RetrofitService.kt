package com.maru.data.network.server

import com.maru.data.model.CoupleInfo
import com.maru.data.model.Route
import com.maru.data.model.SimpleRoute
import com.maru.data.model.User
import com.maru.data.network.*
import com.maru.data.network.request.CreateCoupleRequest
import com.maru.data.network.request.RefreshRequest
import com.maru.data.network.request.SignInRequest
import com.maru.data.network.request.SignUpRequest
import com.maru.data.network.response.SignUpResponse
import okhttp3.RequestBody
import okhttp3.MultipartBody
import retrofit2.http.*

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
    suspend fun getRouteOfMonth(
        @Query("year") year: String,
        @Query("month") month: String
    ): List<SimpleRoute>

    @GET("route/{routeId}")
    suspend fun getRouteById(@Path("routeId") routeId: Int): Route

    @Multipart
    @POST("route")
    @JvmSuppressWildcards
    suspend fun saveNewRoute(
        @PartMap map: Map<String, RequestBody>,
        @Part photos: List<MultipartBody.Part>,
        @Part geoCoord: List<MultipartBody.Part>
    )
}