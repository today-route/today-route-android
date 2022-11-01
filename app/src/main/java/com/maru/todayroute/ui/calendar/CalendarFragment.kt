package com.maru.todayroute.ui.calendar

import android.graphics.Color
import android.os.Bundle
import android.view.View
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.maru.data.model.Route
import com.maru.todayroute.R
import com.maru.todayroute.databinding.FragmentCalendarBinding
import com.maru.todayroute.ui.MainViewModel
import com.maru.todayroute.util.BaseFragment
import com.prolificinteractive.materialcalendarview.CalendarDay
import com.prolificinteractive.materialcalendarview.format.ArrayWeekDayFormatter
import com.prolificinteractive.materialcalendarview.format.MonthArrayTitleFormatter
import dagger.hilt.android.AndroidEntryPoint
import java.util.*

@AndroidEntryPoint
class CalendarFragment : BaseFragment<FragmentCalendarBinding>(R.layout.fragment_calendar) {

    private val calendarViewModel: CalendarViewModel by viewModels()
    private val activityViewModel: MainViewModel by activityViewModels()

    private val routeListAdapter by lazy { RouteListAdapter() }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupRecyclerView()
        setupObserver()
        setupCalendar()
        calendarViewModel.getAllRoute(activityViewModel.coupleInfo.value!!.id)

        // [2] 특정 날짜 선택
        binding.cvCalendarView.setOnDateChangedListener { _, date, _ ->
            // 리사이클러뷰 띄워주기
            calendarViewModel.dateSelected(date, activityViewModel.coupleInfo.value!!.startDate)
        }
    }

    private fun setupObserver() {
        calendarViewModel.selectedDateRouteList.observe(viewLifecycleOwner) { showList ->
            routeListAdapter.submitList(showList)
        }

        calendarViewModel.dDay.observe(viewLifecycleOwner) { dDay ->
            binding.tvDDay.text = "♥+${dDay}일의 기록"
        }

        calendarViewModel.allRouteList.observe(viewLifecycleOwner) { routeList ->
            checkDot(routeList)
        }
    }

    private fun setupCalendar() {
        // [1] 첫화면
        // 1-1 첫화면에서 오늘날짜 표시
        val today = CalendarDay.today()
        binding.cvCalendarView.selectedDate = today
        calendarViewModel.dateSelected(today, activityViewModel.coupleInfo.value!!.startDate)


        // 일요일 빨간글씨
        binding.cvCalendarView.addDecorators(
            SundayDecorator()
        )
        // 월 한글표시
        binding.cvCalendarView.setTitleFormatter(MonthArrayTitleFormatter(resources.getTextArray(R.array.custom_months)));
        // 요일 한글표시
        binding.cvCalendarView.setWeekDayFormatter(ArrayWeekDayFormatter(resources.getTextArray(R.array.custom_weekdays)));
        // 달력 헤더 스타일
        binding.cvCalendarView.setHeaderTextAppearance(R.style.CalendarWidgetHeader);
    }

    // 게시물 있는 날 점찍기
    private fun checkDot(routeList: List<Route>){
        for (i in routeList.indices){
            val eventDate = routeList[i].date.split("-")
            val year = Integer.parseInt(eventDate[0])
            val month = Integer.parseInt(eventDate[1])
            val day = Integer.parseInt(eventDate[2])
            binding.cvCalendarView
                .addDecorator(
                    EventDecorator(
                        Color.parseColor("#FF018786"),
                        Collections.singleton(CalendarDay.from(year, month-1, day))))
        }
    }

    private fun setupRecyclerView() {
        binding.rvTitle.layoutManager =
            LinearLayoutManager(this.context, LinearLayoutManager.VERTICAL, false)
        binding.rvTitle.setHasFixedSize(true)   // 성능개선
        binding.rvTitle.adapter = routeListAdapter
    }
}

