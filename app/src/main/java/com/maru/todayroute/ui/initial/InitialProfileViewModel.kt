package com.maru.todayroute.ui.initial

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.kakao.sdk.user.UserApiClient

class InitialProfileViewModel : ViewModel() {

    val profileImageUrl = MutableLiveData<String>()
    val nickname = MutableLiveData<String>()
    val introduction = MutableLiveData<String>()

    init {
        UserApiClient.instance.me { user, error ->
            if (error == null && user != null) {
                nickname.value = user.kakaoAccount?.profile?.nickname
                profileImageUrl.value = user.kakaoAccount?.profile?.thumbnailImageUrl
            }
        }
    }

    fun bindingImage(imageUri: String) {
        profileImageUrl.value = imageUri
    }
}