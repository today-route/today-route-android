package com.maru.data.datasource

import com.maru.data.model.User
import javax.inject.Inject

class UserRemoteDataSource @Inject constructor() : UserDataSource.Remote {
    override suspend fun addNewUser(user: User) {
        TODO("Not yet implemented")
    }
}