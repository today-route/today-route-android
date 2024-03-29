package com.maru.todayroute.ui.calendar

import android.graphics.Color
import android.text.style.ForegroundColorSpan
import com.prolificinteractive.materialcalendarview.CalendarDay
import com.prolificinteractive.materialcalendarview.DayViewDecorator
import com.prolificinteractive.materialcalendarview.DayViewFacade
import com.prolificinteractive.materialcalendarview.spans.DotSpan
import java.util.*

// 게시물 있는날 표시
class EventDecorator() : DayViewDecorator {

    private var color = 0
    private lateinit var dates: HashSet<CalendarDay>

    constructor(color: Int, dates: Collection<CalendarDay>) : this() {
        this.color = color
        this.dates = HashSet(dates)
    }

    override fun shouldDecorate(day: CalendarDay?): Boolean {
        return dates.contains(day)
    }

    override fun decorate(view: DayViewFacade?) {
        view?.addSpan(DotSpan(7F, color))
    }
}

// 일요일 빨간글씨
class SundayDecorator : DayViewDecorator {
    private val calendar = Calendar.getInstance()
    override fun shouldDecorate(day: CalendarDay): Boolean {
        day.copyTo(calendar)
        val weekDay = calendar[Calendar.DAY_OF_WEEK]
        return weekDay == Calendar.SUNDAY
    }

    override fun decorate(view: DayViewFacade) {
        view.addSpan(ForegroundColorSpan(Color.RED))
    }
}

// 오늘 보라글씨
class TodayDecorator : DayViewDecorator {

    override fun shouldDecorate(day: CalendarDay): Boolean {
        val cal1 = day.calendar
        val cal2 = Calendar.getInstance()

        return cal1[Calendar.ERA] == cal2[Calendar.ERA]
                && cal1[Calendar.YEAR] == cal2[Calendar.YEAR]
                && cal1[Calendar.DAY_OF_YEAR] == cal2[Calendar.DAY_OF_YEAR]
    }

    override fun decorate(view: DayViewFacade) {
        view.addSpan(ForegroundColorSpan(Color.parseColor("#B496D3")))
    }

}