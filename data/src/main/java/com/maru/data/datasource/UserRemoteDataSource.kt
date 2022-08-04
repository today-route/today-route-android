package com.maru.data.datasource

import android.util.Log
import com.maru.data.model.User
import com.maru.data.network.RetrofitInstance
import javax.inject.Inject

class UserRemoteDataSource @Inject constructor() : UserDataSource.Remote {

    override suspend fun addNewUser(user: User): Result<User> = runCatching {
        RetrofitInstance.service.postNewUser(user)
    }
}