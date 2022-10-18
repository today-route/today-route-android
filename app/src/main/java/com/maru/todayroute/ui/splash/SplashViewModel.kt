package com.maru.todayroute.ui.splash

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.auth0.android.jwt.JWT
import com.maru.data.model.User
import com.maru.data.network.Token
import com.maru.data.repository.InitialRepository
import com.maru.data.repository.TokenRepository
import com.maru.data.util.Constants.EMPTY_STRING
import com.maru.todayroute.SignInTokenInfo.token
import com.maru.todayroute.util.SingleLiveEvent
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

@HiltViewModel
class SplashViewModel @Inject constructor(
    private val initialRepository: InitialRepository,
    private val tokenRepository: TokenRepository
) : ViewModel() {

    private val _moveToInitialActivity = SingleLiveEvent<Unit>()
    val moveToInitialActivity: LiveData<Unit> get() = _moveToInitialActivity
    private val _moveToMainActivity = SingleLiveEvent<Unit>()
    val moveToMainActivity: LiveData<Unit> get() = _moveToMainActivity

    suspend fun checkSignInState() {
        checkAccessToken()
    }

    private suspend fun checkAccessToken() {
        val accessToken = withContext(viewModelScope.coroutineContext) {
            tokenRepository.getAccessToken().first()
        }
        val refreshToken = withContext(viewModelScope.coroutineContext) {
            tokenRepository.getRefreshToken().first()
        }

        if (accessToken != EMPTY_STRING) {
            if (isTokenExpired(JWT(accessToken))) {
                checkRefreshToken(refreshToken)
            } else {
                token = Token(accessToken, refreshToken)
                getSignInCoupleInfoByAccessToken()
            }
        } else {
            _moveToInitialActivity.call()
        }
    }

    private suspend fun checkRefreshToken(refreshToken: String) {
        if (isTokenExpired(JWT(refreshToken))) {
            viewModelScope.launch(Dispatchers.IO) {
                tokenRepository.removeTokens()
            }
            _moveToInitialActivity.call()
        } else {
            val newToken = withContext(viewModelScope.coroutineContext) {
                tokenRepository.refresh(refreshToken)
            }
            viewModelScope.launch(Dispatchers.IO) {
                tokenRepository.saveTokens(newToken)
            }
            token = newToken
            getSignInCoupleInfoByAccessToken()
        }
    }

    private fun isTokenExpired(token: JWT): Boolean =
        token.isExpired(10)

    private suspend fun getSignInCoupleInfoByAccessToken() {
        val result = withContext(viewModelScope.coroutineContext) {
            initialRepository.getMyCoupleData()
        }

        if (result.isSuccess) {
            _moveToMainActivity.call()
        } else {
            _moveToInitialActivity.call()
        }
    }
}