package com.maru.todayroute.util

import com.naver.maps.geometry.LatLng
import java.text.SimpleDateFormat
import java.util.*

object Utils {

    fun String.convertSingleToDoubleDigit(): String = if (this.length < 2) "0$this" else this

    fun calculateDDay(startDate: String): String {
        val currentTimeMillis = System.currentTimeMillis()
        val simpleDataFormat = SimpleDateFormat("yyyy-MM-dd", Locale.KOREAN)
        val startDateInMillis = simpleDataFormat.parse(startDate)?.time!!

        return "❤️${(currentTimeMillis - startDateInMillis) / (24 * 60 * 60 * 1000) + 1}❤️"
    }
}

object RouteUtils {
    fun calculateCenterCoordinate(latitudeList: List<Double>, longitudeList: List<Double>): LatLng {
        val minLatitude = latitudeList.minOrNull() ?: 0.0
        val maxLatitude = latitudeList.maxOrNull() ?: 0.0
        val minLongitude = longitudeList.minOrNull() ?: 0.0
        val maxLongitude = longitudeList.maxOrNull() ?: 0.0
        val centerLatitude = (minLatitude + maxLatitude) / 2
        val centerLongitude = (minLongitude + maxLongitude) / 2

        return LatLng(centerLatitude, centerLongitude)
    }
}