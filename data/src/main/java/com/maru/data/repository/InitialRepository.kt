package com.maru.data.repository

import com.maru.data.datasource.initial.InitialDataSource
import com.maru.data.datasource.initial.InitialLocalDataSource
import com.maru.data.datasource.initial.InitialRemoteDataSource
import com.maru.data.model.CoupleInfo
import com.maru.data.model.User
import com.maru.data.network.SignUpRequest
import com.maru.data.network.SignUpResponse
import com.maru.data.network.Token
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

    override suspend fun saveCoupleId(coupleId: Int) {
        userLocalDataSource.saveCoupleId(coupleId)
    }

    override suspend fun getCoupleId(): Flow<Int> =
        userLocalDataSource.getCoupleId()

    override suspend fun registerNewUser(user: SignUpRequest): Result<SignUpResponse> =
        userRemoteDataSource.registerNewUser(user)

    override suspend fun getCodeById(id: Int): Result<String> =
        userRemoteDataSource.getCodeById(id)

    override suspend fun findUserByInviteCode(inviteCode: String): Result<User> =
        userRemoteDataSource.findUserByInviteCode(inviteCode)

    override suspend fun registerNewCouple(coupleInfo: CoupleInfo): Result<CoupleInfo> =
        userRemoteDataSource.registerNewCouple(coupleInfo)

    override suspend fun findCoupleInfoById(id: Int): Result<CoupleInfo> =
        userRemoteDataSource.findCoupleInfoById(id)

    override suspend fun getUserById(id: Int): Result<User> =
        userRemoteDataSource.getUserById(id)

    override suspend fun getCoupleInfoById(id: Int): Result<CoupleInfo> =
        userRemoteDataSource.getCoupleInfoById(id)

    override suspend fun signInUser(key: String): Result<Token> =
        userRemoteDataSource.signInUser(key)


}