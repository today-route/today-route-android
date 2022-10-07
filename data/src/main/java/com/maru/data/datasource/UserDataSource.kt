package com.maru.data.datasource

import com.maru.data.model.CoupleInfo
import com.maru.data.model.User
import com.maru.data.network.RegisterUserRequest
import kotlinx.coroutines.flow.Flow

interface UserDataSource {

    interface Local {
        suspend fun saveSignInUserId(id: Int)
        suspend fun getSignedInUserId(): Flow<Int>
        suspend fun saveCoupleInfo(coupleInfo: CoupleInfo)
    }

    interface Remote {
        suspend fun registerNewUser(user: RegisterUserRequest): Result<User>
        suspend fun getCodeById(id: Int): Result<String>
        suspend fun findUserByInviteCode(inviteCode: String): Result<User>
        suspend fun registerNewCouple(coupleInfo: CoupleInfo): Result<CoupleInfo>
    }
}