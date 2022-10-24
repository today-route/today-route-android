package com.maru.todayroute.util

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