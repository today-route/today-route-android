package com.maru.todayroute.ui.addeditroute

import android.net.Uri
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.maru.data.repository.RouteRepository
import com.maru.todayroute.util.RouteUtils
import com.maru.todayroute.util.SingleLiveEvent
import com.naver.maps.geometry.LatLng
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.io.File
import javax.inject.Inject

@HiltViewModel
class AddRouteViewModel @Inject constructor(
    private val repository: RouteRepository
) : ViewModel() {

    private lateinit var geoCoordList: List<LatLng>

    val drawRoute: LiveData<List<LatLng>> get() = _drawRoute
    private val _drawRoute: MutableLiveData<List<LatLng>> = MutableLiveData()
    val centerCoord: LiveData<LatLng> get() = _centerCoord
    private val _centerCoord: MutableLiveData<LatLng> = MutableLiveData()
    val photoUriList: LiveData<List<Uri>> get() = _photoUriList
    private val _photoUriList: MutableLiveData<List<Uri>> = MutableLiveData(listOf())
    val showToastMessage: LiveData<Unit> get() = _showToastMessage
    private val _showToastMessage: SingleLiveEvent<Unit> = SingleLiveEvent()
    private lateinit var date: String
    val selectedPhotoUriList: MutableLiveData<List<Uri>> = MutableLiveData()

    val location = MutableLiveData("")
    val title = MutableLiveData("")
    val contents = MutableLiveData("")

    fun setDate(date: String) {
        this.date = date
    }

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

    fun addPhotos(newPhotoList: List<Uri>) {
        val photoList = mutableListOf<Uri>()
        val oldPhotoList = _photoUriList.value!!

        if (10 < oldPhotoList.size + newPhotoList.size) {
            _showToastMessage.call()
        } else {
            photoList.addAll(_photoUriList.value!!)
            photoList.addAll(newPhotoList)

            _photoUriList.value = photoList
        }
    }

    fun removePhotoAt(index: Int) {
        val photoList = _photoUriList.value!!.toMutableList()
        photoList.removeAt(index)
        _photoUriList.value = photoList
    }

    fun trySaveNewRoute() {
        selectedPhotoUriList.value = photoUriList.value
    }

    fun saveNewRoute(fileList: List<File>, zoomLevel: Double) {
        viewModelScope.launch(Dispatchers.IO) {
            repository.saveNewRoute(
                date,
                zoomLevel,
                title.value!!,
                contents.value!!,
                location.value!!,
                fileList,
                geoCoordList.map { listOf(it.latitude, it.longitude) }
            )
        }
    }
}