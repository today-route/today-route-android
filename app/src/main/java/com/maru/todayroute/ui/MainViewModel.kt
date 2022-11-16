package com.maru.todayroute.ui

import androidx.lifecycle.*
import com.maru.data.model.CoupleInfo
import com.maru.data.model.User
import com.maru.data.repository.UserRepository
import com.maru.todayroute.util.Utils.calculateDDay
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    private val userRepository: UserRepository
) : ViewModel() {

    val user: LiveData<User> get() = _user
    private val _user: MutableLiveData<User> = MutableLiveData()
    val coupleInfo: LiveData<CoupleInfo> get() = _coupleInfo
    private val _coupleInfo: MutableLiveData<CoupleInfo> = MutableLiveData()

    val dDay: LiveData<String> = Transformations.map(coupleInfo) { calculateDDay(it.startDate) }

    fun fetchMainData() {
        viewModelScope.launch {
            fetchUserData()
            fetchCoupleInfo()
        }
    }

    private suspend fun fetchUserData() {
        viewModelScope.launch(Dispatchers.IO) {
            val result = userRepository.getMyUserData()

            if (result.isSuccess) {
                _user.postValue(result.getOrNull())
            }
        }
    }

    private suspend fun fetchCoupleInfo() {
        viewModelScope.launch(Dispatchers.IO) {
            val result = userRepository.getMyCoupleData()

            if (result.isSuccess) {
                _coupleInfo.postValue(result.getOrNull())
            }
        }
    }
}