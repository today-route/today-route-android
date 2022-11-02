package com.maru.todayroute.ui.route

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.map
import com.maru.data.model.Route
import com.maru.todayroute.R
import com.maru.todayroute.util.Dummy.imageList
import com.maru.todayroute.util.Dummy.routeList
import com.maru.todayroute.util.RouteUtils
import com.naver.maps.geometry.LatLng
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class RouteViewModel @Inject constructor() : ViewModel() {

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
        fetchRoute(routeId)
        fetchPhotoUrlList(routeId)
    }

    fun setGeoCoordsOnMap() {
        fetchGeoCoordList(route.id)
    }

    private fun fetchRoute(routeId: Int) {
        // TODO: server에서 가져온 값 사용하도록 수정
        route = routeList[routeId]
        _date.value = koreanDateFormat(route.date)
        _title.value = route.title
        _contents.value = route.content
        _mapZoomLevel.value = route.zoomLevel
    }

    private fun koreanDateFormat(date: String): String {
        val dateList = date.split("-")
        return "${dateList[0]}년 ${dateList[1]}월 ${dateList[2]}일"
    }

    private fun fetchPhotoUrlList(routeId: Int) {
        // TODO: server에서 가져온 값 사용하도록 수정
        val routePhotoUrlList = mutableListOf(R.drawable.route_image)

        for (routePhoto in imageList) {
            if (routePhoto.routeId == routeId)
                routePhotoUrlList.add(routePhoto.url)
        }

        _photoUrlList.value = routePhotoUrlList
    }

    fun getCenterCoordinate() {
        val latitudeList = geoCoordList.value?.let { list -> list.map { it.latitude } }
        val longitudeList = geoCoordList.value?.let { list -> list.map { it.longitude } }

        _centerCoord.value = RouteUtils.calculateCenterCoordinate(latitudeList!!, longitudeList!!)
    }

    private fun fetchGeoCoordList(routeId: Int) {
        // TODO: server에서 가져온 값 사용하도록 수정
        _geoCoordList.value = listOf(LatLng(37.549043, 126.9254563), LatLng(37.549043, 126.9252463))
    }
}