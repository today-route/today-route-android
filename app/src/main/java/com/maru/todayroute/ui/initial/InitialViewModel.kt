package com.maru.todayroute.ui.initial

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.kakao.sdk.auth.AuthApiClient
import com.kakao.sdk.user.UserApiClient
import com.maru.data.model.Gender
import com.maru.data.network.RegisterUserRequest
import com.maru.data.repository.UserRepository
import com.maru.todayroute.util.SingleLiveEvent
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.withContext
import javax.inject.Inject

@HiltViewModel
class InitialViewModel @Inject constructor(
    private val userRepository: UserRepository
) : ViewModel() {

    private lateinit var key: String
    private lateinit var gender: Gender
    private lateinit var email: String
    private lateinit var profileUrl: String
    private lateinit var nickname: String
    private lateinit var birthday: String

    private val _moveToConnectCoupleFragment = SingleLiveEvent<Unit>()
    val moveToConnectCoupleFragment: LiveData<Unit> get() = _moveToConnectCoupleFragment
    private val _moveToInitialUserInfoFragment = SingleLiveEvent<Unit>()
    val moveToInitialUserInfoFragment: LiveData<Unit> get() = _moveToInitialUserInfoFragment

    private var inviteCode: String? = null

    init {
        if (AuthApiClient.instance.hasToken()) {
            setUserInfoFromKakaoApi()
        }
    }

    fun setUserInfoFromKakaoApi() {
        with(UserApiClient.instance) {
            me { user, error ->
                if (error == null && user != null) {
                    email = user.kakaoAccount?.email ?: ""
                    nickname = user.kakaoAccount?.profile?.nickname ?: ""
                    profileUrl = user.kakaoAccount?.profile?.thumbnailImageUrl ?: ""
                    Log.e("HI", "$email $nickname $profileUrl")
                }
            }
            accessTokenInfo { tokenInfo, error ->
                if (error == null && tokenInfo != null) {
                    key = tokenInfo.id.toString()
                    Log.e("HI", "$key")
                }
            }
        }
        _moveToInitialUserInfoFragment.call()
    }

    fun setUserGender(gender: Gender) {
        this.gender = gender
    }

    fun setUserBirthday(birthday: String) {
        this.birthday = birthday
    }

    fun setInviteCode(inviteCode: String?) {
        this.inviteCode = inviteCode
    }

    suspend fun registerNewUser() {
        val result = withContext(viewModelScope.coroutineContext) {
            userRepository.registerNewUser(
                RegisterUserRequest(
                    key,
                    gender,
                    email,
                    nickname,
                    profileUrl,
                    birthday
                )
            )
        }
//        if (result.isSuccess) {
//            val id = result.getOrNull()!!.id
            userRepository.saveSignInUserId(1) // TODO: parameter로 id 넘기기
            _moveToConnectCoupleFragment.call()
//        }
    }

    suspend fun checkUserInfo() {
        val id = withContext(viewModelScope.coroutineContext) {
            userRepository.getSignedInUserId().first()
        }
        Log.d("HHII", "$id")
        if (id != -1) {
            _moveToConnectCoupleFragment.call()
        }
    }
}