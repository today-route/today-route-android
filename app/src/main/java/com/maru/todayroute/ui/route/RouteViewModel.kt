package com.maru.todayroute.ui.route

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.maru.data.model.Route
import com.maru.data.repository.RouteRepository
import com.maru.todayroute.R
import com.maru.todayroute.util.RouteUtils
import com.maru.todayroute.util.SingleLiveEvent
import com.naver.maps.geometry.LatLng
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject
import kotlin.coroutines.coroutineContext

@HiltViewModel
class RouteViewModel @Inject constructor(
    private val repository: RouteRepository
) : ViewModel() {

    private lateinit var route: Route
    val photoUrlList: LiveData<List<String>> get() = _photoUrlList
    private val _photoUrlList: MutableLiveData<List<String>> = MutableLiveData()
    val geoCoordList: LiveData<List<LatLng>> get() = _geoCoordList
    private val _geoCoordList: MutableLiveData<List<LatLng>> = MutableLiveData()

    val date: MutableLiveData<String> = MutableLiveData()
    val title: MutableLiveData<String> = MutableLiveData()
    val contents: MutableLiveData<String> = MutableLiveData()
    val location: MutableLiveData<String> = MutableLiveData()

    val mapZoomLevel: LiveData<Double> get() = _mapZoomLevel
    private val _mapZoomLevel: MutableLiveData<Double> = MutableLiveData()

    val centerCoord: LiveData<LatLng> get() = _centerCoord
    private val _centerCoord: MutableLiveData<LatLng> = MutableLiveData()

    val moveToPreviousFragment: LiveData<Unit> get() = _moveToPreviousFragment
    private val _moveToPreviousFragment: SingleLiveEvent<Unit> = SingleLiveEvent()

    suspend fun setRouteDiaryData(routeId: Int) {
        getRoute(routeId)
    }

    private fun setGeoCoordsOnMap() {
        getGeoCoordList()
    }

    private suspend fun getRoute(routeId: Int) {
        val result = withContext(viewModelScope.coroutineContext) {
            repository.getRouteById(routeId)
        }

        if (result.isSuccess) {
            route = result.getOrNull()!!
            date.value = koreanDateFormat(route.date)
            title.value = route.title
            contents.value = route.content
            location.value = route.location
            _mapZoomLevel.value = route.zoomLevel

            val photoList = mutableListOf(R.drawable.route_image.toString())
            photoList.addAll(route.routePhoto.map { it.url })
            _photoUrlList.value = photoList

            setGeoCoordsOnMap()
            getCenterCoordinate()
        }
    }

    private fun koreanDateFormat(date: String): String {
        val dateList = date.split("-")
        return "${dateList[0]}년 ${dateList[1]}월 ${dateList[2].substring(0..1)}일"
    }

    private fun getCenterCoordinate() {
        val latitudeList = geoCoordList.value?.let { list -> list.map { it.latitude } }
        val longitudeList = geoCoordList.value?.let { list -> list.map { it.longitude } }

        _centerCoord.value = RouteUtils.calculateCenterCoordinate(latitudeList!!, longitudeList!!)
    }

    private fun getGeoCoordList() {
        _geoCoordList.value = route.geoCoord.map { LatLng(it[0], it[1]) }
    }

    suspend fun deleteRoute(routeId: Int) {
        val result = withContext(viewModelScope.coroutineContext) {
            repository.deleteRoute(routeId)
        }
        if (result.isSuccess) {
            _moveToPreviousFragment.call()
        }
    }
}