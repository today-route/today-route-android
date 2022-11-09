package com.maru.todayroute.ui.calendar

import android.os.Bundle
import android.view.View
import androidx.core.view.isVisible
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.maru.data.model.SimpleRoute
import com.maru.todayroute.R
import com.maru.todayroute.databinding.FragmentCalendarBinding
import com.maru.todayroute.ui.MainViewModel
import com.maru.todayroute.util.BaseFragment
import com.prolificinteractive.materialcalendarview.CalendarDay
import com.prolificinteractive.materialcalendarview.format.ArrayWeekDayFormatter
import com.prolificinteractive.materialcalendarview.format.MonthArrayTitleFormatter
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
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
    }

    private fun setupObserver() {
        calendarViewModel.selectedDateRouteList.observe(viewLifecycleOwner) { showList ->
            routeListAdapter.submitList(showList)
        }

        calendarViewModel.dDay.observe(viewLifecycleOwner) { dDay ->
            binding.tvDDay.text = "♥+${dDay}일의 기록"
        }

        calendarViewModel.routeOfCurrentMonthList.observe(viewLifecycleOwner) { routeList ->
            checkDot(routeList)
        }
    }

    private fun setupCalendar() {
        // [1] 첫화면
        // 1-1 첫화면에서 오늘날짜 표시
        val today = CalendarDay.today()
        lifecycleScope.launch(Dispatchers.IO) {
            calendarViewModel.getRouteOfMonth(today.year, today.month + 1)
        }


        // 일요일 빨간글씨
        with(binding.cvCalendarView) {
            addDecorators(
                SundayDecorator(),
                TodayDecorator()
            )
            setTitleFormatter(MonthArrayTitleFormatter(resources.getTextArray(R.array.custom_months))) // 월 한글 표시
            setWeekDayFormatter(ArrayWeekDayFormatter(resources.getTextArray(R.array.custom_weekdays))) // 요일 한글 표시
            setHeaderTextAppearance(R.style.CalendarWidgetHeader) // 달력 헤더 스타일
            // 달 변경
            setOnMonthChangedListener { _, date ->
                lifecycleScope.launch(Dispatchers.IO) {
                    calendarViewModel.getRouteOfMonth(date.year, date.month + 1)
                }
            }
            // 특정 날짜 선택
            setOnDateChangedListener { _, date, _ ->
                binding.tvDDay.isVisible = true
                calendarViewModel.dateSelected(date, activityViewModel.coupleInfo.value!!.startDate)
            }
        }
    }

    // 게시물 있는 날 점찍기
    private fun checkDot(routeList: List<SimpleRoute>) {
        for (i in routeList.indices) {
            val eventDate = routeList[i].date.split("-")
            val year = Integer.parseInt(eventDate[0])
            val month = Integer.parseInt(eventDate[1])
            val day = Integer.parseInt(eventDate[2].substring(0..1))
            binding.cvCalendarView
                .addDecorator(
                    EventDecorator(
                        requireContext().getColor(R.color.purple),
                        Collections.singleton(CalendarDay.from(year, month - 1, day))
                    )
                )
        }
    }

    private fun setupRecyclerView() {
        binding.rvTitle.layoutManager =
            LinearLayoutManager(this.context, LinearLayoutManager.VERTICAL, false)
        binding.rvTitle.setHasFixedSize(true)   // 성능개선
        binding.rvTitle.adapter = routeListAdapter
    }
}

