package com.maru.data.datasource

import com.maru.data.model.User
import com.maru.data.network.RegisterUserRequest
import kotlinx.coroutines.flow.Flow

interface UserDataSource {

    interface Local {
        suspend fun saveSignInUserId(id: Int)
        suspend fun getSignedInUserId(): Flow<Int>
    }

    interface Remote {
        suspend fun registerNewUser(user: RegisterUserRequest): Result<User>
    }
}