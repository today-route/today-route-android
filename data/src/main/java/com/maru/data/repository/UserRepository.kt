package com.maru.data.repository

import com.maru.data.datasource.user.UserDataSource
import com.maru.data.datasource.user.UserRemoteDataSource
import com.maru.data.model.CoupleInfo
import com.maru.data.model.SimpleCoupleInfo
import com.maru.data.model.User
import com.maru.data.network.request.SignUpRequest
import com.maru.data.network.response.SignUpResponse
import com.maru.data.network.Token
import java.io.File
import javax.inject.Inject

class UserRepository @Inject constructor(
    private val userRemoteDataSource: UserDataSource
) : UserDataSource {

    override suspend fun registerNewUser(user: SignUpRequest): Result<SignUpResponse> =
        userRemoteDataSource.registerNewUser(user)

    override suspend fun signInUser(key: String): Result<Token> =
        userRemoteDataSource.signInUser(key)

    override suspend fun registerNewCouple(code: String, startDate: String): Result<Unit> =
        userRemoteDataSource.registerNewCouple(code, startDate)

    override suspend fun getMyCoupleData(): Result<CoupleInfo> =
        userRemoteDataSource.getMyCoupleData()

    override suspend fun getMyUserData(): Result<User> =
        userRemoteDataSource.getMyUserData()

    override suspend fun editCoupleStartDate(startDate: String): Result<SimpleCoupleInfo> =
        userRemoteDataSource.editCoupleStartDate(startDate)

    override suspend fun editUser(profile: File, nickname: String, birthday: String): Result<User> =
        userRemoteDataSource.editUser(profile, nickname, birthday)

    override suspend fun breakUpCouple(isEnd: Boolean): Result<SimpleCoupleInfo> =
        userRemoteDataSource.breakUpCouple(isEnd)

    override suspend fun deleteUser() = userRemoteDataSource.deleteUser()
}