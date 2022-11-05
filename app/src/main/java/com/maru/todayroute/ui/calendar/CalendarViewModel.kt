package com.maru.todayroute.ui.calendar

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.maru.data.model.SimpleRoute
import com.maru.data.network.response.RouteOfMonthResponse
import com.maru.data.repository.RouteRepository
import com.maru.todayroute.util.Dummy.routeList
import com.maru.todayroute.util.Dummy.simpleRouteList
import com.maru.todayroute.util.Utils.convertSingleToDoubleDigit
import com.prolificinteractive.materialcalendarview.CalendarDay
import dagger.hilt.android.lifecycle.HiltViewModel
import java.text.SimpleDateFormat
import javax.inject.Inject

@HiltViewModel
class CalendarViewModel @Inject constructor(
    private val routeRepository: RouteRepository
) : ViewModel() {

    val selectedDateRouteList: LiveData<List<SimpleRoute>> get() = _selectedDateRouteList
    private val _selectedDateRouteList: MutableLiveData<List<SimpleRoute>> = MutableLiveData()
    val routeOfCurrentMonthList: LiveData<List<SimpleRoute>> get() = _routeOfCurrentMonthList
    private val _routeOfCurrentMonthList: MutableLiveData<List<SimpleRoute>> = MutableLiveData()
    val dDay: LiveData<String> get() = _dDay
    private val _dDay: MutableLiveData<String> = MutableLiveData()

    fun getRouteOfMonth(year: Int, month: Int) {

        val showList = mutableListOf<SimpleRoute>()

        for(route in simpleRouteList){
            val dateInList = route.date.split("-")

            if(year == dateInList[0].toInt() && month == dateInList[1].toInt()){
                showList.add(route)
            }
        }
        _routeOfCurrentMonthList.value = simpleRouteList
//        viewModelScope.launch(Dispatchers.IO) {
//            val result = routeRepository.getRouteOfMonth(year, month)
//
//            if (result.isSuccess) {
//                _routeOfCurrentMonthList.postValue(result.getOrNull()!!)
//            }
//        }
    }

    fun dateSelected(date: CalendarDay, startDate: String) {
        setRouteListOfSelectedDate(date)
        setDDay(date, startDate)
    }

    private fun setRouteListOfSelectedDate(date: CalendarDay) {
        val showList: MutableList<SimpleRoute> = mutableListOf()

        for(i in simpleRouteList.indices){
            val eventDate = routeList[i].date.split("-")
            val year = Integer.parseInt(eventDate[0])
            val month = Integer.parseInt(eventDate[1])
            val day = Integer.parseInt(eventDate[2])

            if(day==date.day && month-1==date.month && year==date.year){
                showList.add(simpleRouteList[i])
            }
        }

        _selectedDateRouteList.value = showList
    }

    private fun setDDay(date: CalendarDay, startDate: String) {
        val dateInString = "${date.year}-${(date.month + 1).toString().convertSingleToDoubleDigit()}-${date.day.toString().convertSingleToDoubleDigit()}"

        // 디데이 계산
        _dDay.value = calculateDDay(dateInString, startDate)
    }

    private fun calculateDDay(selectedDate: String, startDate: String): String {
        val dateFormat = SimpleDateFormat("yyyy-MM-dd")

        val startDate = dateFormat.parse(startDate).time
        val clickDate = dateFormat.parse(selectedDate).time

        return ((clickDate - startDate) / (24 * 60 * 60 * 1000) + 1).toString()
    }
}