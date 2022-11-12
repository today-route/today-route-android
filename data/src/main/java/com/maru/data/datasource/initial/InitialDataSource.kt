package com.maru.data.datasource.initial

import com.maru.data.model.CoupleInfo
import com.maru.data.model.SimpleCoupleInfo
import com.maru.data.model.User
import com.maru.data.network.request.SignUpRequest
import com.maru.data.network.response.SignUpResponse
import com.maru.data.network.Token
import java.io.File

interface InitialDataSource {
        suspend fun registerNewUser(user: SignUpRequest): Result<SignUpResponse>
        suspend fun signInUser(key: String): Result<Token>
        suspend fun registerNewCouple(code: String, startDate: String): Result<Unit>
        suspend fun getMyCoupleData(): Result<CoupleInfo>
        suspend fun getMyUserData(): Result<User>
        suspend fun editCoupleStartDate(startDate: String): Result<SimpleCoupleInfo>
        suspend fun editUser(profile: File, nickname: String, birthday: String): Result<User>
}