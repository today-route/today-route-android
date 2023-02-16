package com.maru.todayroute.ui.home

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.maru.todayroute.ui.home.TrackingInfo.addGeoCoord
import com.maru.todayroute.ui.home.TrackingInfo.clearGeoCoord
import com.maru.todayroute.ui.home.TrackingInfo.currentLocation
import com.maru.todayroute.ui.home.TrackingInfo.geoCoordList
import com.maru.todayroute.ui.home.TrackingInfo.isSameLocation
import com.maru.todayroute.util.SingleLiveEvent
import com.maru.todayroute.util.Utils.getCurrentDate
import com.naver.maps.geometry.LatLng
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor() : ViewModel() {

    val isForegroundServiceRunning: LiveData<Boolean> get() = _isForegroundServiceRunning
    private val _isForegroundServiceRunning: MutableLiveData<Boolean> = MutableLiveData()
    val showOverlayOnCurrentLocation: LiveData<LatLng> get() = _showOverlayOnCurrentLocation
    private val _showOverlayOnCurrentLocation: MutableLiveData<LatLng> = MutableLiveData()
    val updateUserLocation: LiveData<Boolean> get() = _updateUserLocation
    private val _updateUserLocation: MutableLiveData<Boolean> = MutableLiveData(true)
    val updatePath: LiveData<List<LatLng>> get() = _updatePath
    private val _updatePath: SingleLiveEvent<List<LatLng>> = SingleLiveEvent()
    val moveMapCameraToCurrentLocation: LiveData<LatLng> get() = _moveMapCameraToCurrentLocation
    private val _moveMapCameraToCurrentLocation: MutableLiveData<LatLng> = MutableLiveData()

    val moveToAddRouteFragment: LiveData<Pair<Array<LatLng>, String>> get() = _moveToAddRouteFragment
    private val _moveToAddRouteFragment: SingleLiveEvent<Pair<Array<LatLng>, String>> = SingleLiveEvent()

    val date get() = getCurrentDate()

    fun startRecording() {
        if (TrackingInfo.isRecording) {
            TrackingInfo.isRecording = false
            _isForegroundServiceRunning.value = false
            if (isValidRecord()) {
                _updateUserLocation.value = false
                _moveToAddRouteFragment.value = Pair(geoCoordList.toTypedArray(), date)
                clearGeoCoord()
//                        TODO: ViewModel에 기록 정보 저장하고 루트 추가 화면으로 넘어가도록 요청
            } else {
//                Toast.makeText(
//                    requireContext(),
//                    "유효하지 않은 기록입니다. 다시 기록해주세요.",
//                    Toast.LENGTH_SHORT
//                ).show()
            }
        } else {
            TrackingInfo.isRecording = true
            _isForegroundServiceRunning.value = true
            initGeoCoordList()
        }
    }

    fun moveMapCameraToCurrentLocation() {
        _moveMapCameraToCurrentLocation.value = currentLocation
    }

    fun initCurrentLocation(currentLocation: LatLng) {
        TrackingInfo.currentLocation = currentLocation
        moveMapCameraToCurrentLocation()
        _showOverlayOnCurrentLocation.value = currentLocation
    }

    fun userMoves(latitude: Double, longitude: Double) {
        Log.d("location", "$latitude $longitude")
        if (TrackingInfo.isRecording) {
            showPath()
        } else {
            currentLocation = LatLng(latitude, longitude)
        }
        _showOverlayOnCurrentLocation.value = currentLocation
    }

    fun showPath() {
        if (isValidRecord()) {
            _updatePath.value = geoCoordList
        }
    }

    private fun isValidRecord(): Boolean = geoCoordList.size >= 2

    private fun initGeoCoordList() {
        clearGeoCoord()
        addGeoCoord(currentLocation.latitude, currentLocation.longitude)
    }
}