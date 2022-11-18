package com.maru.data.network.server

import com.maru.data.model.*
import com.maru.data.network.*
import com.maru.data.network.request.*
import com.maru.data.network.response.EditRouteResponse
import com.maru.data.network.response.SaveNewRouteResponse
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
    ): SaveNewRouteResponse

    @PATCH("couple")
    suspend fun editCoupleStartDate(
        @Body startDate: EditCoupleStartDateRequest
    ): SimpleCoupleInfo

    @Multipart
    @PATCH("user")
    @JvmSuppressWildcards
    suspend fun editUser(
        @Part profile: MultipartBody.Part,
        @Part("nickname") nickname: RequestBody,
        @Part("birthday") birthday: RequestBody
    ): User

    @PATCH("couple")
    suspend fun breakUpCouple(
        @Body idEnd: BreakUpCoupleRequest
    ): SimpleCoupleInfo

    @Multipart
    @PATCH("route/{routeId}")
    @JvmSuppressWildcards
    suspend fun editRoute(
        @Path("routeId") routeId: Int,
        @PartMap map: Map<String, RequestBody>,
        @Part photos: List<MultipartBody.Part>,
    ): EditRouteResponse

    @DELETE("user")
    suspend fun deleteUser()
}