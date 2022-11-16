package com.maru.todayroute.ui.mypage

import android.graphics.Bitmap
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.maru.data.model.User
import com.maru.data.repository.UserRepository
import com.maru.todayroute.util.ImageHandler
import com.maru.todayroute.util.SingleLiveEvent
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.withContext
import javax.inject.Inject

@HiltViewModel
class EditUserViewModel @Inject constructor(
    private val userRepository: UserRepository
) : ViewModel() {

    val user: LiveData<User> get() = _user
    private val _user: MutableLiveData<User> = MutableLiveData()
    private lateinit var profileImageBitmap: Bitmap
    private var isFromGallery = false
    val moveToPreviousFragment: LiveData<Unit> get() = _moveToPreviousFragment
    private val _moveToPreviousFragment: SingleLiveEvent<Unit> = SingleLiveEvent()

    fun setUserData(user: User) {
        _user.value = user
    }

    fun setProfileImageBitmap(bitmap: Bitmap) {
        this.profileImageBitmap = bitmap
    }

    fun isFromGallery() {
        isFromGallery = true
    }

    suspend fun editUser(nickname: String, birthday: String) {
        val profile = if (isFromGallery) {
            ImageHandler.optimizeImage(profileImageBitmap, 70)
        } else {
            ImageHandler.optimizeImage(profileImageBitmap, 100)
        }

        val result = withContext(viewModelScope.coroutineContext) {
            profile?.let { userRepository.editUser(profile, nickname, birthday) }
        }

        if (result != null) {
            if (result.isSuccess) {
                _moveToPreviousFragment.call()
            }
        }
    }
}