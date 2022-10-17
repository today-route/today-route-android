package com.maru.data.datasource.initial

import com.maru.data.model.CoupleInfo
import com.maru.data.model.User
import com.maru.data.network.SignUpRequest
import com.maru.data.network.SignUpResponse
import com.maru.data.network.Token
import kotlinx.coroutines.flow.Flow

interface InitialDataSource {
        suspend fun registerNewUser(user: SignUpRequest): Result<SignUpResponse>
        suspend fun signInUser(key: String): Result<Token>
        suspend fun registerNewCouple(code: String, startDate: String): Result<CoupleInfo>
        suspend fun getMyCoupleData(): Result<CoupleInfo>
        suspend fun getMyUserData(): Result<User>
}