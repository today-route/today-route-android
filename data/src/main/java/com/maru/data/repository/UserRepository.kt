package com.maru.data.repository

import com.maru.data.datasource.UserDataSource
import com.maru.data.datasource.UserLocalDataSource
import com.maru.data.datasource.UserRemoteDataSource
import com.maru.data.model.User
import javax.inject.Inject

class UserRepository @Inject constructor(
    private val userLocalDataSource: UserLocalDataSource,
    private val userRemoteDataSource: UserRemoteDataSource
) : UserDataSource.Local, UserDataSource.Remote {

    override suspend fun signInUser(user: User) {
        TODO("Not yet implemented")
    }

    override suspend fun getSignedInUser(): User {
        TODO("Not yet implemented")
    }

    override suspend fun addNewUser(user: User): Result<User> {
        return userRemoteDataSource.addNewUser(user)
    }
}