package com.maru.todayroute.ui.mypage

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class EditCoupleStartDateViewModel @Inject constructor() : ViewModel() {

    val startDate: LiveData<String> get() = _startDate
    private val _startDate: MutableLiveData<String> = MutableLiveData()

    fun setStartDate(startDate: String) {
        _startDate.value = startDate
    }
}