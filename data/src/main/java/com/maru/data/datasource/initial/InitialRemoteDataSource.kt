package com.maru.data.datasource.initial

import com.maru.data.model.CoupleInfo
import com.maru.data.model.User
import com.maru.data.network.request.SignInRequest
import com.maru.data.network.request.SignUpRequest
import com.maru.data.network.response.SignUpResponse
import com.maru.data.network.Token
import com.maru.data.network.firebase.FirebaseHelper
import com.maru.data.network.request.CreateCoupleRequest
import com.maru.data.network.server.RetrofitService
import javax.inject.Inject

class InitialRemoteDataSource @Inject constructor(
    private val retrofitService: RetrofitService,
    private val firebaseHelper: FirebaseHelper
) : InitialDataSource {

    override suspend fun registerNewUser(user: SignUpRequest): Result<SignUpResponse> = runCatching {
        retrofitService.registerNewUser(user)
//          firebaseHelper.registerNewUser(user)
    }

    override suspend fun signInUser(key: String): Result<Token> = runCatching {
        retrofitService.signInUser(SignInRequest(key))
    }

    override suspend fun registerNewCouple(code: String, startDate: String): Result<Unit> = runCatching {
        retrofitService.createCouple(CreateCoupleRequest(code, startDate))
    }

    override suspend fun getMyCoupleData(): Result<CoupleInfo> = runCatching {
        retrofitService.getMyCoupleData()
    }

    override suspend fun getMyUserData(): Result<User> = kotlin.runCatching {
        retrofitService.getMyUserData()
    }
}