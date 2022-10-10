package com.maru.todayroute.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.maru.data.model.CoupleInfo
import com.maru.data.model.User
import com.maru.data.repository.InitialRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    private val initialRepository: InitialRepository
) : ViewModel() {

    private lateinit var user: User
    private lateinit var coupleInfo: CoupleInfo

    fun fetchMainData() {
        viewModelScope.launch {
            fetchUserData()
            fetchCoupleInfo()
        }
    }

    private suspend fun fetchUserData() {
        val userId = withContext(viewModelScope.coroutineContext) {
            initialRepository.getSignedInUserId().first()
        }

        withContext(Dispatchers.IO) {
            user = initialRepository.getUserById(userId).getOrNull()!!
            println(user)
        }
    }

    private suspend fun fetchCoupleInfo() {
        val coupleId = withContext(viewModelScope.coroutineContext) {
            initialRepository.getCoupleId().first()
        }

        withContext(Dispatchers.IO) {
            coupleInfo = initialRepository.getCoupleInfoById(coupleId).getOrNull()!!
            println(coupleInfo)
        }
    }
}