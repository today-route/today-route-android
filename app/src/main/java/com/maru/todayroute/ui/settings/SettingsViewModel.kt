package com.maru.todayroute.ui.settings

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.kakao.sdk.user.UserApiClient
import com.maru.data.repository.TokenRepository
import com.maru.todayroute.util.SingleLiveEvent
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

@HiltViewModel
class SettingsViewModel @Inject constructor(
    private val tokenRepository: TokenRepository
) : ViewModel() {

    val moveToInitialActivity: LiveData<Unit> get() = _moveToInitialActivity
    private val _moveToInitialActivity: SingleLiveEvent<Unit> = SingleLiveEvent()
    val showToastMessage: LiveData<String> get() = _showToastMessage
    private val _showToastMessage: MutableLiveData<String> = MutableLiveData()

    fun signOut() {
        UserApiClient.instance.logout { _ ->
            viewModelScope.launch(Dispatchers.IO) {
                tokenRepository.removeTokens()
                withContext(Dispatchers.Main) {
                    _showToastMessage.postValue("로그아웃 되었습니다.")
                    _moveToInitialActivity.call()
                }
            }
        }
    }
}