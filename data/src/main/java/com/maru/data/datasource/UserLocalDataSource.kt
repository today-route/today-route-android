package com.maru.data.datasource

import com.maru.data.model.User
import javax.inject.Inject

class UserLocalDataSource @Inject constructor() : UserDataSource.Local {
    override suspend fun signInUser(user: User) {
        TODO("Not yet implemented")
    }

    override suspend fun getSignedInUser(): User {
        TODO("Not yet implemented")
    }
}