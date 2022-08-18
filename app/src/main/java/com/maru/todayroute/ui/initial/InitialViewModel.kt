package com.maru.todayroute.ui.initial

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.kakao.sdk.user.UserApiClient
import com.maru.data.model.User
import com.maru.data.repository.UserRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class InitialViewModel @Inject constructor (
    private val userRepository: UserRepository
) : ViewModel() {

    private lateinit var email: String
    private lateinit var key: String
    val profileImageUrl = MutableLiveData<String>()
    val nickname = MutableLiveData<String>()
    val introduction = MutableLiveData<String>()

    init {
        with (UserApiClient.instance) {
            me { user, error ->
                if (error == null && user != null) {
                    email = user.kakaoAccount?.email ?: ""
                    nickname.value = user.kakaoAccount?.profile?.nickname
                    profileImageUrl.value = user.kakaoAccount?.profile?.thumbnailImageUrl
                }
            }
            accessTokenInfo { tokenInfo, error ->
                if (error == null && tokenInfo != null) {
                    key = tokenInfo.id.toString()
                }
            }
        }

    }

    fun bindingImage(imageUri: String) {
        profileImageUrl.value = imageUri
    }

    fun addNewUser() {
        viewModelScope.launch {
            userRepository.addNewUser(User(key, email, nickname.value!!, introduction.value ?: "", profileImageUrl.value!!))
        }
    }
}