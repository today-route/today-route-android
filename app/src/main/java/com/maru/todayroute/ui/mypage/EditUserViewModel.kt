package com.maru.todayroute.ui.mypage

import android.net.Uri
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.maru.data.model.User
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class EditUserViewModel @Inject constructor() : ViewModel() {

    val user: LiveData<User> get() = _user
    private val _user: MutableLiveData<User> = MutableLiveData()

    fun setUserData(user: User) {
        _user.value = user
    }
}