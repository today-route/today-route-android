package com.maru.todayroute.ui.addeditroute

import android.net.Uri
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.maru.data.model.Route
import com.maru.data.repository.RouteRepository
import com.maru.todayroute.util.RouteUtils
import com.maru.todayroute.util.SingleLiveEvent
import com.naver.maps.geometry.LatLng
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.withContext
import javax.inject.Inject

@HiltViewModel
class EditRouteViewModel @Inject constructor(private val repository: RouteRepository) :
    ViewModel() {

    private lateinit var route: Route
    val title: MutableLiveData<String> = MutableLiveData()
    val content: MutableLiveData<String> = MutableLiveData()
    val location: MutableLiveData<String> = MutableLiveData()
    val geoCoordList: LiveData<List<LatLng>> get() = _geoCoordList
    private val _geoCoordList: MutableLiveData<List<LatLng>> = MutableLiveData()
    val photoUriList: LiveData<List<Uri>> get() = _photoUriList
    private val _photoUriList: MutableLiveData<List<Uri>> = MutableLiveData()

    val mapSettings: LiveData<Pair<LatLng, Double>> get() = _mapSettings
    private val _mapSettings: MutableLiveData<Pair<LatLng, Double>> = MutableLiveData()
    val showToastMessage: LiveData<Unit> get() = _showToastMessage
    private val _showToastMessage: SingleLiveEvent<Unit> = SingleLiveEvent()

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

    suspend fun setRouteData(routeId: Int) {
        val result = withContext(viewModelScope.coroutineContext) {
            repository.getRouteById(routeId)
        }

        if (result.isSuccess) {
            route = result.getOrNull()!!
            title.value = route.title
            content.value = route.content
            location.value = route.location

            val photoList = mutableListOf<Uri>()
            photoList.addAll(route.routePhoto.map { Uri.parse(it.url) })
            _photoUriList.value = photoList

            getGeoCoordList()
            getCenterCoordinate()
        }
    }

    private fun getCenterCoordinate() {
        val latitudeList = geoCoordList.value?.let { list -> list.map { it.latitude } }
        val longitudeList = geoCoordList.value?.let { list -> list.map { it.longitude } }

        _mapSettings.value = Pair(
            RouteUtils.calculateCenterCoordinate(latitudeList!!, longitudeList!!),
            route.zoomLevel
        )
    }

    private fun getGeoCoordList() {
        _geoCoordList.value = route.geoCoord.map { LatLng(it[0], it[1]) }
    }
}