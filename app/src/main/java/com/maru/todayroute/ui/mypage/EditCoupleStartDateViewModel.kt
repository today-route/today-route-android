package com.maru.todayroute.ui.mypage

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.maru.data.repository.InitialRepository
import com.maru.todayroute.util.SingleLiveEvent
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.withContext
import javax.inject.Inject

@HiltViewModel
class EditCoupleStartDateViewModel @Inject constructor(
    private val userRepository: InitialRepository
) : ViewModel() {

    val startDate: LiveData<String> get() = _startDate
    private val _startDate: MutableLiveData<String> = MutableLiveData()
    val moveToPreviousFragment: LiveData<Unit> get() = _moveToPreviousFragment
    private val _moveToPreviousFragment: SingleLiveEvent<Unit> = SingleLiveEvent()

    fun setStartDate(startDate: String) {
        _startDate.value = startDate
    }

    suspend fun editCoupleStartDate(startDate: String) {
        val result = withContext(viewModelScope.coroutineContext) {
            userRepository.editCoupleStartDate(startDate)
        }

        if (result.isSuccess) {
            _moveToPreviousFragment.call()
        }
    }
}