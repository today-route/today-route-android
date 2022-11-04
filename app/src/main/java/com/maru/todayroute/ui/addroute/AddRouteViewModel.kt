package com.maru.todayroute.ui.addroute

import android.graphics.Bitmap
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.maru.todayroute.util.RouteUtils
import com.maru.todayroute.util.SingleLiveEvent
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
    val photoBitmapList: LiveData<List<Bitmap>> get() = _photoBitmapList
    private val _photoBitmapList: MutableLiveData<List<Bitmap>> = MutableLiveData(listOf())
    val showToastMessage: LiveData<Unit> get() = _showToastMessage
    private val _showToastMessage: SingleLiveEvent<Unit> = SingleLiveEvent()

    fun setGeoCoordList(geoCoordList: List<LatLng>) {
        this.geoCoordList = geoCoordList
    }

    fun drawRoute() {
        _drawRoute.value = geoCoordList
    }

    fun getCenterCoordinate() {
        val latitudeList = geoCoordList.map { it.latitude }
        val longitudeList = geoCoordList.map { it.longitude }

        _centerCoord.value = RouteUtils.calculateCenterCoordinate(latitudeList, longitudeList)
    }

    fun addPhotos(newPhotoList: List<Bitmap>) {
        val photoList = mutableListOf<Bitmap>()
        val oldPhotoList = _photoBitmapList.value!!

        if (10 < oldPhotoList.size + newPhotoList.size) {
            _showToastMessage.call()
        } else {
            photoList.addAll(_photoBitmapList.value!!)
            photoList.addAll(newPhotoList)

            _photoBitmapList.value = photoList
        }
    }
}