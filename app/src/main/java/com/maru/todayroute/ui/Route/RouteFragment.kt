package com.maru.todayroute.ui.Route

import android.os.Bundle
import android.view.View
import androidx.navigation.fragment.navArgs
import com.maru.todayroute.R
import com.maru.todayroute.databinding.FragmentCalendarBinding
import com.maru.todayroute.databinding.FragmentRouteBinding
import com.maru.todayroute.databinding.ItemRouteDiaryBinding
import com.maru.todayroute.ui.calendar.Route
import com.maru.todayroute.ui.calendar.SundayDecorator
import com.maru.todayroute.util.BaseFragment
import com.prolificinteractive.materialcalendarview.CalendarDay
import com.prolificinteractive.materialcalendarview.format.ArrayWeekDayFormatter
import com.prolificinteractive.materialcalendarview.format.MonthArrayTitleFormatter
import com.maru.todayroute.ui.calendar.*

class RouteFragment : BaseFragment<FragmentRouteBinding>(R.layout.fragment_route) {

    private val args: RouteFragmentArgs by navArgs()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        fun dateFormat(route: Route) : List<String>{

            val eventDate = route.date.split("-")
            return eventDate
        }

        binding.apply{
            val route: Route = routeList[args.routeId]
            val dates: List<String> = dateFormat(route)

            binding.tvRouteDateTop.text = "${dates.get(0)}년 ${dates.get(1)}월 ${dates.get(2)}일"
            binding.tvRouteTitle.text = route.title         // 제목
            binding.tvLocation.text = route.location        // 위치
            //binding.ivRoute = route.지도                   // 루트
            //binding.rvRouteImage = route.사진              // 사진
            binding.etInsertContent.text = route.content    // 본문내용
        }




    }
}