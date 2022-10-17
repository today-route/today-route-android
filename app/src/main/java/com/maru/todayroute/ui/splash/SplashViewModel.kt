package com.maru.todayroute.ui.splash

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.auth0.android.jwt.JWT
import com.maru.data.model.User
import com.maru.data.repository.TokenRepository
import com.maru.todayroute.util.SingleLiveEvent
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

class SplashViewModel @Inject constructor(
    private val repository: TokenRepository
) : ViewModel() {

    private val _moveToInitialActivity = SingleLiveEvent<User?>()
    val moveToInitialActivity: LiveData<User?> get() = _moveToInitialActivity
    private val _moveToMainActivity = SingleLiveEvent<Unit>()
    val moveToMainActivity: LiveData<Unit> get() = _moveToMainActivity

    suspend fun checkSignInState() {
        checkAccessToken()
    }

    private suspend fun checkAccessToken() {
        val accessToken = withContext(viewModelScope.coroutineContext) {
            repository.getAccessToken().first()
        }

        if (isTokenExpired(JWT(accessToken))) {
           checkRefreshToken()
        } else {
//            getSignInUserCoupleInfoByAccessToken()
        }
    }

    private suspend fun checkRefreshToken() {
        val refreshToken = withContext(viewModelScope.coroutineContext) {
            repository.getRefreshToken().first()
        }

        if (isTokenExpired(JWT(refreshToken))) {
            viewModelScope.launch(Dispatchers.IO) {
                repository.removeTokens()
            }
            _moveToInitialActivity.value = null
        } else {
            val newToken = withContext(viewModelScope.coroutineContext) {
                repository.refresh(refreshToken)
            }
            viewModelScope.launch(Dispatchers.IO) {
                repository.saveTokens(newToken)
            }
//            getSignInUserCoupleInfoByAccessToken()
        }
    }

    private fun isTokenExpired(token: JWT): Boolean =
        token.isExpired(10)

//    private fun getSignInUserCoupleInfoByAccessToken() {
//        // TODO: access token으로 요청했을 때 user, couple 정보 받아오기
//    }
}