package com.maru.todayroute.ui.calendar

import java.util.Date

/*
 data 클래스
 - 오버라이딩 없이 이콜스 해쉬 투스트링 자동으로 해줌
 -
 */

data class Route(val id:Int,
            val distance: Int,
            val movingTime: Int,
            val userId: Int,
            val date: Date?,
            val zoomLevel: Int,
            val title: String,
            val content: Char,
            val location: Char){
}