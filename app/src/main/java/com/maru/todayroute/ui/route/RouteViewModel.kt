package com.maru.todayroute.ui.route

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.maru.data.model.Route
import com.maru.data.repository.RouteRepository
import com.maru.todayroute.R
import com.maru.todayroute.util.Dummy.routeList
import com.maru.todayroute.util.RouteUtils
import com.naver.maps.geometry.LatLng
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class RouteViewModel @Inject constructor(
    private val repository: RouteRepository
) : ViewModel() {

    private lateinit var route: Route
    val photoUrlList: LiveData<List<Int>> get() = _photoUrlList // TODO: 서버 연결 후 String으로 변경
    private val _photoUrlList: MutableLiveData<List<Int>> = MutableLiveData()
    val geoCoordList: LiveData<List<LatLng>> get() = _geoCoordList
    private val _geoCoordList: MutableLiveData<List<LatLng>> = MutableLiveData()

    val date: LiveData<String> get() = _date
    private val _date: MutableLiveData<String> = MutableLiveData()
    val title: LiveData<String> get() = _title
    private val _title: MutableLiveData<String> = MutableLiveData()
    val contents: LiveData<String> get() = _contents
    private val _contents: MutableLiveData<String> = MutableLiveData()
    val mapZoomLevel: LiveData<Int> get() = _mapZoomLevel
    private val _mapZoomLevel: MutableLiveData<Int> = MutableLiveData()

    val centerCoord: LiveData<LatLng> get() = _centerCoord
    private val _centerCoord: MutableLiveData<LatLng> = MutableLiveData()

    fun setRouteDiaryData(routeId: Int) {
        getRoute(routeId)
    }

    fun setGeoCoordsOnMap() {
        getGeoCoordList()
    }

    private fun getRoute(routeId: Int) {
        // TODO: server에서 가져온 값 사용하도록 수정
        route = routeList[routeId]
        _date.value = koreanDateFormat(route.date)
        _title.value = route.title
        _contents.value = route.content
        _mapZoomLevel.value = route.zoomLevel

        val photoList = mutableListOf(R.drawable.route_image)
        photoList.addAll(route.photoList)
        _photoUrlList.value = photoList
    }

    private fun koreanDateFormat(date: String): String {
        val dateList = date.split("-")
        return "${dateList[0]}년 ${dateList[1]}월 ${dateList[2]}일"
    }

    fun getCenterCoordinate() {
        val latitudeList = geoCoordList.value?.let { list -> list.map { it.latitude } }
        val longitudeList = geoCoordList.value?.let { list -> list.map { it.longitude } }

        _centerCoord.value = RouteUtils.calculateCenterCoordinate(latitudeList!!, longitudeList!!)
    }

    private fun getGeoCoordList() {
        // _geoCoordList.value = route.geoCoordList.map { LatLng(it[0], it[1]) }
        _geoCoordList.value = listOf(LatLng(37.549043, 126.9254563), LatLng(37.549043, 126.9252463))
    }
}