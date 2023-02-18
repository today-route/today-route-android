package com.maru.todayroute.ui.home

import androidx.lifecycle.MutableLiveData
import com.maru.todayroute.BuildConfig
import com.maru.todayroute.ui.home.TrackingInfo.currentLocation
import com.naver.maps.geometry.LatLng
import kotlin.math.roundToInt

object TrackingInfo {

    const val ACTION_RECORDING_STOP = "${BuildConfig.APPLICATION_ID}.stop"

    val isRecording: MutableLiveData<Boolean> = MutableLiveData(false)
    lateinit var currentLocation: LatLng
    val geoCoordList get() = _geoCoordList
    private val _geoCoordList: MutableList<LatLng> = mutableListOf()

    fun addGeoCoord(latitude: Double, longitude: Double) {
        _geoCoordList.add(LatLng(latitude, longitude))
        currentLocation = LatLng(latitude, longitude)
    }

    fun clearGeoCoord() {
        _geoCoordList.clear()
    }

    fun isSameLocation(newLatitude: Double, newLongitude: Double): Boolean {
        val preLatitude = (currentLocation.latitude * 10000).roundToInt()
        val preLongitude = (currentLocation.longitude * 10000).roundToInt()
        val targetLatitude = (newLatitude * 10000).roundToInt()
        val targetLongitude = (newLongitude * 10000).roundToInt()

        return preLatitude == targetLatitude && preLongitude == targetLongitude
    }
}