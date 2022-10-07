package com.maru.data.repository

import com.maru.data.datasource.UserDataSource
import com.maru.data.datasource.UserLocalDataSource
import com.maru.data.datasource.UserRemoteDataSource
import com.maru.data.model.CoupleInfo
import com.maru.data.model.User
import com.maru.data.network.RegisterUserRequest
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class UserRepository @Inject constructor(
    private val userLocalDataSource: UserLocalDataSource,
    private val userRemoteDataSource: UserRemoteDataSource
) : UserDataSource.Local, UserDataSource.Remote {

    override suspend fun saveSignInUserId(id: Int) {
        userLocalDataSource.saveSignInUserId(id)
    }

    override suspend fun getSignedInUserId(): Flow<Int> =
        userLocalDataSource.getSignedInUserId()

    override suspend fun saveCoupleInfo(coupleInfo: CoupleInfo) {
        userLocalDataSource.saveCoupleInfo(coupleInfo)
    }

    override suspend fun registerNewUser(user: RegisterUserRequest): Result<User> =
        userRemoteDataSource.registerNewUser(user)

    override suspend fun getCodeById(id: Int): Result<String> =
        userRemoteDataSource.getCodeById(id)

    override suspend fun findUserByInviteCode(inviteCode: String): Result<User> =
        userRemoteDataSource.findUserByInviteCode(inviteCode)

    override suspend fun registerNewCouple(coupleInfo: CoupleInfo): Result<CoupleInfo> =
        userRemoteDataSource.registerNewCouple(coupleInfo)

}