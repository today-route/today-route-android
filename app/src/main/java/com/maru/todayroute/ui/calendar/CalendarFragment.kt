package com.maru.todayroute.ui.calendar

import android.graphics.Color
import android.os.Bundle
import android.text.style.ForegroundColorSpan
import android.util.Log
import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import com.maru.todayroute.R
import com.maru.todayroute.databinding.FragmentCalendarBinding
import com.maru.todayroute.util.BaseFragment
import com.prolificinteractive.materialcalendarview.CalendarDay
import com.prolificinteractive.materialcalendarview.DayViewDecorator
import com.prolificinteractive.materialcalendarview.DayViewFacade
import com.prolificinteractive.materialcalendarview.format.ArrayWeekDayFormatter
import com.prolificinteractive.materialcalendarview.format.MonthArrayTitleFormatter
import com.prolificinteractive.materialcalendarview.spans.DotSpan
import java.text.SimpleDateFormat
import java.util.*

val routeList = arrayListOf(
    Route(0,0,3,0,"2022-09-03",1, "1.오늘은 내생일! 홍대,합정 데이트", "정말정말 좋았던 오늘 !!\n 1.오늘은 내생일! 홍대,합정 데이트", "마포구 서교동"),
    Route(1,0,3,1,"2022-09-06",1, "2.정월대보름 축제 ", "정말정말 좋았던 오늘 !!\n 2.정월대보름 축제", "수원시 행궁동"),
    Route(2,0,3,2,"2022-09-08",1, "3.여의도 불꽃 축제", "정말정말 좋았던 오늘 !!\n 3.여의도 불꽃 축제", "서울특별시 영등포구 여의도동"),
    Route(3,0,3,3,"2022-10-03",1, "4.스키대신 눈썰매만 타고온날", "정말정말 좋았던 오늘 !!\n 4.스키대신 눈썰매만 타고온날", "강원도 홍천군 비발디파크"),
    Route(4,0,3,4,"2022-10-11",1, "5.제주도 한바퀴 돌고온 날", "정말정말 좋았던 오늘 !!\n 5.제주도 한바퀴 돌고온 날", "제주특별자치도 제주시"),
    Route(5,0,3,5,"2022-10-23",1, "6.날씨좋은날 화담숲 드라이브", "정말정말 좋았던 오늘 !!\n 6.날씨좋은날 화담숲 드라이브", "경기 광주시 도척면 도웅리"),
    Route(6,0,3,6,"2022-11-11",1, "7.정월대보름 축제 ", "정말정말 좋았던 오늘 !!\n 7.정월대보름 축제", "수원시 행궁동"),
    Route(7,0,3,7,"2022-12-22",1, "8.여의도 불꽃 축제", "정말정말 좋았던 오늘 !!\n 8.여의도 불꽃 축제", "영등포구 여의도동"),
    Route(8,0,3,8,"2022-10-15",1, "9.스키대신 눈썰매만 타고온날", "정말정말 좋았던 오늘 !!\n 9.스키대신 눈썰매만 타고온날", "경기 광주시 곤지암리조트"),
    Route(9,0,3,9,"2022-10-22",1, "10.제주도 한바퀴 돌고온 날", "정말정말 좋았던 오늘 !!\n 10.제주도 한바퀴 돌고온 날", "제주시 애월읍"),
    Route(10,0,3,10,"2022-10-23",1, "11.완전 가을날씨.. 한강에서 피크닉", "정말정말 좋았던 오늘 !!\n 11.완전 가을날씨.. 한강에서 피크닉", "망원 한강공원"),
    Route(11,0,3,11,"2022-10-23",1, "12.밥먹고 배불러서 연남동 산책", "정말정말 좋았던 오늘 !!\n 12.밥먹고 배불러서 연남동 산책", "마포구 연남동"),
    Route(12,0,3,12,"2022-10-23",1, "13.홍대, 망원, 연남, 연희 소품샵 구경", "정말정말 좋았던 오늘 !!\n 13.홍대, 망원, 연남, 연희 소품샵 구경", "마포구"),
    Route(13,0,3,13,"2022-10-23",1, "14.아이폰사러 성북구 갔다온날", "정말정말 좋았던 오늘 !!\n 14.아이폰사러 성북구 갔다온날", "성북구 능동"),
    Route(14,0,3,14,"2022-10-23",1, "15.오늘은 알찬 하루~~", "정말정말 좋았던 오늘 !!\n 15.오늘은 알찬 하루~~", "마포구 상수동"),
    Route(15,0,3,15,"2022-10-23",1, "16.남이섬 당일치기!!!!! 춘천까지 다녀옴", "정말정말 좋았던 오늘 !!\n 16.남이섬 당일치기!!!!! 춘천까지 다녀옴", "강원도 춘천시 남산면"),
    Route(16,0,3,16,"2022-10-23",1, "17.대전 성심당 다 돌기", "정말정말 좋았던 오늘 !!\n 17.대전 성심당 다 돌기", "대전광역시 동구 중앙로 215"),

    )

// 게시물 있는날 표시
class EventDecorator() : DayViewDecorator {

    private var color = 0
    private lateinit var dates : HashSet<CalendarDay>

    constructor(color: Int, dates: Collection<CalendarDay>) : this() {
        this.color=color
        this.dates=HashSet(dates)
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

class CalendarFragment : BaseFragment<FragmentCalendarBinding>(R.layout.fragment_calendar) {
    val firstDate = "20171211"

    // 디데이 계산
    fun dDay(clickdate: String): Long {
        val dateFormat = SimpleDateFormat("yyyyMMdd")

        val startDate = dateFormat.parse(firstDate).time
        val clickDate = dateFormat.parse(clickdate).time

        return (clickDate - startDate) / (24 * 60 * 60 * 1000) + 1
    }

    // 게시물 있는 날 점찍기
    fun CheckDot(routeList: ArrayList<Route>){
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

    // 리사이클러뷰 띄우기
    fun showrecycler(routeList: ArrayList<Route>, date: CalendarDay){
        var showList: ArrayList<Route> = ArrayList<Route>()

        for(i in routeList.indices){
            val eventDate = routeList[i].date.split("-")
            val year = Integer.parseInt(eventDate[0])
            val month = Integer.parseInt(eventDate[1])
            val day = Integer.parseInt(eventDate[2])

            if(day==date.day && month-1==date.month && year==date.year){
                showList.add(routeList[i])
            }
        }
        binding.rvTitle.layoutManager =
            LinearLayoutManager(this.context, LinearLayoutManager.VERTICAL, false)
        binding.rvTitle.setHasFixedSize(true)   // 성능개선
        binding.rvTitle.adapter = RouteAdapter(showList)
    }




    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


        // [1] 첫화면
        // 1-1 첫화면에서 오늘날짜 표시
        binding.cvCalendarView.setSelectedDate(CalendarDay.today())

        // 2022 10월 형태
        //binding.cvCalendarView.setHeaderTextAppearance(R.style.CalendarWidgetHeader);

        // 일요일 빨간글씨
        binding.cvCalendarView.addDecorators(
            SundayDecorator(),
        )
        // 월 한글표시
        binding.cvCalendarView.setTitleFormatter(MonthArrayTitleFormatter(getResources().getTextArray(R.array.custom_months)));
        // 요일 한글표시
        binding.cvCalendarView.setWeekDayFormatter(ArrayWeekDayFormatter(getResources().getTextArray(R.array.custom_weekdays)));
        // 달력 헤더 스타일
        binding.cvCalendarView.setHeaderTextAppearance(R.style.CalendarWidgetHeader);

        // 1-2 오늘날짜기준 dday표시


        // 1-3 @서버에서 현재 달에 등록된 루트 가져오기
        /*
          1) 현재 달에 등록된 루트 게시물 가져오기
          2) 그 날짜에 점찍기
         */
        CheckDot(routeList)






// [2] 특정 날짜 선택
        binding.cvCalendarView.setOnDateChangedListener { widget, date, selected ->
            // 2-1 디데이 표시
            // 선택한 날짜 저장
            val dates: String
            if (date.month + 1 < 10) {
                dates = "${date.year}0${date.month + 1}${date.day}"
            } else
                dates = "${date.year}${date.month + 1}${date.day}"
            //println("${date.year}/${date.month + 1}/${date.day}")

            // 디데이 계산
            val dday = dDay(dates)

            binding.tvDDay.setText("♥+${dday}일의 기록")
            //binding.tvDDay.visibility = View.VISIBLE -> 이거 해줘야함..?

            // 2-2 해당 날짜에 루트가 존재하면 루트리스트(제목) 바인딩해주기
            // 리사이클러뷰 띄워주기
            showrecycler(routeList, date)
        }

    }
}

