package com.maru.data.datasource.user

import com.maru.data.model.CoupleInfo
import com.maru.data.model.SimpleCoupleInfo
import com.maru.data.model.User
import com.maru.data.network.response.SignUpResponse
import com.maru.data.network.Token
import com.maru.data.network.firebase.FirebaseHelper
import com.maru.data.network.request.*
import com.maru.data.network.server.RetrofitService
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody
import okhttp3.RequestBody.Companion.asRequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import java.io.File
import javax.inject.Inject

class UserRemoteDataSource @Inject constructor(
    private val retrofitService: RetrofitService,
    private val firebaseHelper: FirebaseHelper
) : UserDataSource {

    override suspend fun registerNewUser(user: SignUpRequest): Result<SignUpResponse> = runCatching {
        retrofitService.registerNewUser(user)
//          firebaseHelper.registerNewUser(user)
    }

    override suspend fun signInUser(key: String): Result<Token> = runCatching {
        retrofitService.signInUser(SignInRequest(key))
    }

    override suspend fun registerNewCouple(code: String, startDate: String): Result<Unit> = runCatching {
        retrofitService.createCouple(CreateCoupleRequest(code, startDate))
    }

    override suspend fun getMyCoupleData(): Result<CoupleInfo> = runCatching {
        retrofitService.getMyCoupleData()
    }

    override suspend fun getMyUserData(): Result<User> = runCatching {
        retrofitService.getMyUserData()
    }

    override suspend fun editCoupleStartDate(startDate: String): Result<SimpleCoupleInfo> = runCatching {
        retrofitService.editCoupleStartDate(EditCoupleStartDateRequest(startDate))
    }

    override suspend fun editUser(profile: File, nickname: String, birthday: String): Result<User> = runCatching {
            val nicknameBody = stringToPlainTextRequestBody(nickname)
            val birthdayBody = stringToPlainTextRequestBody(birthday)
            val profileBody = profile.asRequestBody("multipart/form-data".toMediaTypeOrNull())
            val profileToUpload = MultipartBody.Part.createFormData("profile",profile.name, profileBody)

            retrofitService.editUser(profileToUpload, nicknameBody, birthdayBody)
    }

    override suspend fun breakUpCouple(isEnd: Boolean): Result<SimpleCoupleInfo> = runCatching {
        retrofitService.breakUpCouple(BreakUpCoupleRequest(isEnd))
    }

    override suspend fun deleteUser() = retrofitService.deleteUser()

    companion object {
        val stringToPlainTextRequestBody: (String) -> RequestBody = { s: String ->
            s.toRequestBody("text/plain".toMediaTypeOrNull())
        }
    }
}