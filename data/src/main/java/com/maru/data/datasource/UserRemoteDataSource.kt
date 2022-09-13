package com.maru.data.datasource

import com.maru.data.model.User
import com.maru.data.network.RegisterUserRequest
import com.maru.data.network.RetrofitService
import javax.inject.Inject

class UserRemoteDataSource @Inject constructor(
    private val retrofitService: RetrofitService
) : UserDataSource.Remote {

    override suspend fun registerNewUser(user: RegisterUserRequest): Result<User> = runCatching {
        retrofitService.registerNewUser(user)
    }
}