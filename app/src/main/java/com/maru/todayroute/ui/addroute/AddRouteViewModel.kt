package com.maru.todayroute.ui.addroute

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.naver.maps.geometry.LatLng
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class AddRouteViewModel @Inject constructor() : ViewModel() {

    private lateinit var geoCoordList: List<LatLng>

    val drawRoute: LiveData<List<LatLng>> get() = _drawRoute
    private val _drawRoute: MutableLiveData<List<LatLng>> = MutableLiveData()
    val centerCoord: LiveData<LatLng> get() = _centerCoord
    private val _centerCoord: MutableLiveData<LatLng> = MutableLiveData()

    fun setGeoCoordList(geoCoordList: List<LatLng>) {
        this.geoCoordList = geoCoordList
    }

    fun drawRoute() {
        _drawRoute.value = geoCoordList
    }

    fun getCenterCoordinate() {
        val latitudeList = geoCoordList.map { it.latitude }
        val longitudeList = geoCoordList.map { it.longitude }
        val minLatitude = latitudeList.minOrNull() ?: 0.0
        val maxLatitude = latitudeList.maxOrNull() ?: 0.0
        val minLongitude = longitudeList.minOrNull() ?: 0.0
        val maxLongitude = longitudeList.maxOrNull() ?: 0.0
        val centerLatitude = (minLatitude + maxLatitude) / 2
        val centerLongitude = (minLongitude + maxLongitude) / 2

        _centerCoord.value = LatLng(centerLatitude, centerLongitude)
    }
}