package com.maru.todayroute.util

object Utils {

    fun String.convertSingleToDoubleDigit(): String = if (this.length < 2) "0$this" else this
}