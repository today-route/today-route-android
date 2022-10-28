package com.maru.todayroute.ui.home

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.maru.todayroute.util.SingleLiveEvent
import com.naver.maps.geometry.LatLng
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlin.math.roundToInt

@HiltViewModel
class HomeViewModel @Inject constructor() : ViewModel() {

    val isRecording: LiveData<Boolean> get() = _isRecording
    private var _isRecording: MutableLiveData<Boolean> = MutableLiveData(false)
    private val geoCoordList: MutableList<LatLng> = mutableListOf()

    private lateinit var currentLocation: LatLng

    val showOverlayOnCurrentLocation: LiveData<LatLng> get() = _showOverlayOnCurrentLocation
    private val _showOverlayOnCurrentLocation: MutableLiveData<LatLng> = MutableLiveData()
    val updateUserLocation: LiveData<Boolean> get() = _updateUserLocation
    private val _updateUserLocation: MutableLiveData<Boolean> = MutableLiveData(true)
    val updatePath: LiveData<List<LatLng>> get() = _updatePath
    private val _updatePath: MutableLiveData<List<LatLng>> = MutableLiveData()
    val moveMapCameraToCurrentLocation: LiveData<LatLng> get() = _moveMapCameraToCurrentLocation
    private val _moveMapCameraToCurrentLocation: MutableLiveData<LatLng> = MutableLiveData()

    val moveToAddRouteFragment: LiveData<Array<LatLng>> get() = _moveToAddRouteFragment
    private val _moveToAddRouteFragment: SingleLiveEvent<Array<LatLng>> = SingleLiveEvent()

    fun startRecording() {
        if (_isRecording.value!!) {
            _isRecording.value = false
            if (isValidRecord()) {
                _updateUserLocation.value = false
                _moveToAddRouteFragment.value = geoCoordList.toTypedArray()
//                        TODO: ViewModel에 기록 정보 저장하고 루트 추가 화면으로 넘어가도록 요청
            } else {
//                Toast.makeText(
//                    requireContext(),
//                    "유효하지 않은 기록입니다. 다시 기록해주세요.",
//                    Toast.LENGTH_SHORT
//                ).show()
            }
        } else {
            _isRecording.value = true
            initGeoCoordList()
        }
    }

    fun moveMapCameraToCurrentLocation() {
        _moveMapCameraToCurrentLocation.value = currentLocation
    }

    fun initCurrentLocation(currentLocation: LatLng) {
        this.currentLocation = currentLocation
        moveMapCameraToCurrentLocation()
        _showOverlayOnCurrentLocation.value = currentLocation
    }

    fun userMoves(latitude: Double, longitude: Double) {

        if (!isSameLocation(latitude, longitude)) {
            Log.d("location", "$latitude $longitude")
            if (_isRecording.value!!) {
                geoCoordList.add(LatLng(latitude, longitude))
                if (isValidRecord()) {
                    _updatePath.value = geoCoordList
                }
            }
            currentLocation = LatLng(latitude, longitude)
            _showOverlayOnCurrentLocation.value = currentLocation
        }
    }

    private fun isSameLocation(newLatitude: Double, newLongitude: Double): Boolean {
        val preLatitude = (currentLocation.latitude * 10000).roundToInt()
        val preLongitude = (currentLocation.longitude * 10000).roundToInt()
        val targetLatitude = (newLatitude * 10000).roundToInt()
        val targetLongitude = (newLongitude * 10000).roundToInt()

        return preLatitude == targetLatitude && preLongitude == targetLongitude
    }

    private fun isValidRecord(): Boolean = geoCoordList.size >= 2

    private fun initGeoCoordList() {
        geoCoordList.clear()
        geoCoordList.add(LatLng(currentLocation.latitude, currentLocation.longitude))
    }
}