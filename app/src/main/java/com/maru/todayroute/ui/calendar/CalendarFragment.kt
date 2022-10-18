package com.maru.todayroute.ui.calendar

import android.os.Bundle
import android.view.View
import android.widget.CalendarView
import androidx.recyclerview.widget.LinearLayoutManager
import com.maru.todayroute.R
import com.maru.todayroute.databinding.FragmentCalendarBinding
import com.maru.todayroute.util.BaseFragment
import java.text.SimpleDateFormat
import java.util.*

class CalendarFragment : BaseFragment<FragmentCalendarBinding>(R.layout.fragment_calendar) {
    val firstDate = "20171211"

    fun dDay(clickdate: String): Long {
        val dateFormat = SimpleDateFormat("yyyyMMdd")

        val startDate = dateFormat.parse(firstDate).time
        val clickDate = dateFormat.parse(clickdate).time

        return (clickDate-startDate)/(24*60*60*1000)+1
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


        // 프래그먼트 생명주기 메소드
        // 뷰가 다 만들어졌을때 호출되는 함수
        // 뷰 관련 작업(리스너 등) 다 여기서 이루어짐.

        // binding.cvCalendar 리스너 같은애들 달기
        // 리사이클러뷰도 여기다가 ㄱ
        // 어댑터는 캘린더 패키지에 만들기

        /*
         1. 현재 달에 해당하는 루트 서버에서 가져오기
          1) 현재 달에 등록된 루트 게시물 가져오기
          2) 그 날짜에 점찍기
         */



        /*
        2. <, > 선택시
         1) < 선택시 -> (현재-1) 달 기준으로 1. 번 진행
         2) > 선택시 -> (현재+1) 달 기준으로 1. 번 진행
         */

        /*
        3. 달력에서 특정 날짜 선택했을때 DDAY표시
         1) 오늘날짜 가져오기
         2) 선택한 날짜 가져오기
         3) [ 1)오늘 - 2)선택 ] 일수 만큼 달력 아래 +DDAY표시
         */

        /*val cal: Calendar = Calendar.getInstance()

        calendarView.setOnDateChangeListener { calendarView, year, month, day ->
            cal.set(Calendar.YEAR, year)
            cal.set(Calendar.MONTH, month)
            cal.set(Calendar.DAY_OF_MONTH, day)
        }*/

        binding.cvCalendarView.setOnDateChangeListener(CalendarView.OnDateChangeListener { view, year, month, day ->
            val date: String
            // 선택한 날짜 저장
            if(month+1<10)
                date = "${year}0${month + 1}${day}" // 2022 09 20
            else
                date = "${year}${month + 1}${day}"
            val dday = dDay(date)

            binding.tvDDay.setText("+♥${dday}")

        })



        val routeList = arrayListOf(
            Route(0,0,3,0, Date(),1, "1.날씨좋은날 화담숲 드라이브", '0', '0'),
            Route(1,0,3,1,null,1, "2.정월대보름 축제 ", '0', '0'),
            Route(2,0,3,2,null,1, "3.여의도 불꽃 축제", '0', '0'),
            Route(3,0,3,3,null,1, "4.스키대신 눈썰매만 타고온날", '0', '0'),
            Route(4,0,3,4,null,1, "5.제주도 한바퀴 돌고온 날", '0', '0'),
            Route(5,0,3,5,null,1, "6.날씨좋은날 화담숲 드라이브", '0', '0'),
            Route(6,0,3,6,null,1, "7.정월대보름 축제 ", '0', '0'),
            Route(7,0,3,7,null,1, "8.여의도 불꽃 축제", '0', '0'),
            Route(8,0,3,8,null,1, "9.스키대신 눈썰매만 타고온날", '0', '0'),
            Route(9,0,3,9,null,1, "10.제주도 한바퀴 돌고온 날", '0', '0')

        )



        binding.rvTitle.layoutManager = LinearLayoutManager(this.context, LinearLayoutManager.VERTICAL, false)
        binding.rvTitle.setHasFixedSize(true)   // 성능개선
        binding.rvTitle.adapter = RouteAdapter(routeList)

    }


}