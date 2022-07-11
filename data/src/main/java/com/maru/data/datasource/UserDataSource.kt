package com.maru.data.datasource

import com.maru.data.model.User

interface UserDataSource {

    interface Local {
        suspend fun signInUser(user: User)
        suspend fun getSignedInUser(): User
    }

    interface Remote {
        suspend fun addNewUser(user: User)
    }
}