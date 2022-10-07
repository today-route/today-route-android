package com.maru.data.repository

import com.maru.data.datasource.initial.InitialDataSource
import com.maru.data.datasource.initial.InitialLocalDataSource
import com.maru.data.datasource.initial.InitialRemoteDataSource
import com.maru.data.model.CoupleInfo
import com.maru.data.model.User
import com.maru.data.network.RegisterUserRequest
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class InitialRepository @Inject constructor(
    private val userLocalDataSource: InitialLocalDataSource,
    private val userRemoteDataSource: InitialRemoteDataSource
) : InitialDataSource.Local, InitialDataSource.Remote {

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

    override suspend fun findCoupleInfoById(id: Int): Result<CoupleInfo> =
        userRemoteDataSource.findCoupleInfoById(id)


}