package com.maru.data.datasource

import com.maru.data.model.CoupleInfo
import com.maru.data.model.User
import com.maru.data.network.RegisterUserRequest
import com.maru.data.network.firebase.FirebaseHelper
import com.maru.data.network.server.RetrofitService
import javax.inject.Inject

class UserRemoteDataSource @Inject constructor(
    private val retrofitService: RetrofitService,
    private val firebaseHelper: FirebaseHelper
) : UserDataSource.Remote {

    override suspend fun registerNewUser(user: RegisterUserRequest): Result<User> = runCatching {
//        retrofitService.registerNewUser(user)
          firebaseHelper.registerNewUser(user)
    }

    override suspend fun getCodeById(id: Int): Result<String> = runCatching {
        firebaseHelper.getCodeById(id)
    }

    override suspend fun findUserByInviteCode(inviteCode: String): Result<User> = runCatching {
        firebaseHelper.findUserByInviteCode(inviteCode)
    }

    override suspend fun registerNewCouple(coupleInfo: CoupleInfo): Result<CoupleInfo> = runCatching {
        firebaseHelper.registerNewCouple(coupleInfo)
    }
}