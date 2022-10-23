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

// 게시물 있는날 표시
//class PostDecorator(datesList: ArrayList<Route>) : DayViewDecorator{
//    var dateList: ArrayList<Route> = datesList
//    private val calendar = Calendar.getInstance()
//
//    override fun shouldDecorate(day: CalendarDay?): Boolean {
//        day?.copyTo(calendar)
//        val weekDay = calendar[Calendar.DAY_OF_MONTH]
//        return weekDay == Calendar.SUNDAY
//    }
//
//    override fun decorate(view: DayViewFacade?) {
//        view?.addSpan(DotSpan(5F, Color.parseColor("#1D872A")))
//    }
//}

//class EventDecorator(dates: Collection<CalendarDay>): DayViewDecorator {
//
//    var dates: HashSet<CalendarDay> = HashSet(dates)
//
//    override fun shouldDecorate(day: CalendarDay?): Boolean {
//        return dates.contains(day)
//    }
//
//    override fun decorate(view: DayViewFacade?) {
//        view?.addSpan(DotSpan(5F, Color.parseColor("#1D872A")))
//    }
//}

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

    fun showrecycler(routeList: ArrayList<Route>, date: CalendarDay){
        var showList: ArrayList<Route> = ArrayList<Route>()

        for(i in routeList.indices){
            val eventDate = routeList[i].date.split("-")
            val year = Integer.parseInt(eventDate[0])
            val month = Integer.parseInt(eventDate[1])
            val day = Integer.parseInt(eventDate[2])

            //Log.d("aaa","${day}${date.day}${month-1}${date.month}${year}${}")
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

        val routeList = arrayListOf(
            Route(0,0,3,0,"2022-09-03",1, "1.날씨좋은날 화담숲 드라이브", '0', '0'),
            Route(1,0,3,1,"2022-09-06",1, "2.정월대보름 축제 ", '0', '0'),
            Route(2,0,3,2,"2022-09-08",1, "3.여의도 불꽃 축제", '0', '0'),
            Route(3,0,3,3,"2022-10-03",1, "4.스키대신 눈썰매만 타고온날", '0', '0'),
            Route(4,0,3,4,"2022-10-11",1, "5.제주도 한바퀴 돌고온 날", '0', '0'),
            Route(5,0,3,5,"2022-10-23",1, "6.날씨좋은날 화담숲 드라이브", '0', '0'),
            Route(6,0,3,6,"2022-11-11",1, "7.정월대보름 축제 ", '0', '0'),
            Route(7,0,3,7,"2022-12-22",1, "8.여의도 불꽃 축제", '0', '0'),
            Route(8,0,3,8,"2022-10-15",1, "9.스키대신 눈썰매만 타고온날", '0', '0'),
            Route(9,0,3,9,"2022-10-22",1, "10.제주도 한바퀴 돌고온 날", '0', '0'),
            Route(5,0,3,10,"2022-10-23",1, "11.날씨좋은날 한강에서 피크닉", '0', '0'),
            Route(5,0,3,11,"2022-10-23",1, "12.밥먹고 배불러서 연남동 산책", '0', '0'),
            Route(5,0,3,12,"2022-10-23",1, "13.홍대, 망원, 연남, 연희 소품샵 구경", '0', '0'),
            Route(5,0,3,13,"2022-10-23",1, "14.아이폰사러 성북구 갔다온날", '0', '0'),
            Route(5,0,3,14,"2022-10-23",1, "15.오늘은 알찬 하루~~", '0', '0'),
            Route(5,0,3,15,"2022-10-23",1, "16.남이섬 당일치기!!!!! 춘천까지 다녀옴", '0', '0'),
            Route(5,0,3,16,"2022-10-23",1, "17.대전 성심당 다 돌기", '0', '0'),

        )

        // 프래그먼트 생명주기 메소드
        // 뷰가 다 만들어졌을때 호출되는 함수
        // 뷰 관련 작업(리스너 등) 다 여기서 이루어짐.

        // binding.cvCalendar 리스너 같은애들 달기
        // 리사이클러뷰도 여기다가 ㄱ
        // 어댑터는 캘린더 패키지에 만들기

        // [1] 첫화면
        // 1-1 첫화면에서 오늘날짜 표시
        binding.cvCalendarView.setSelectedDate(CalendarDay.today())
        binding.cvCalendarView.setHeaderTextAppearance(R.style.CalendarWidgetHeader);
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


//        for (i in routeList.indices){
//            val eventDate = routeList[i].date.split("-")
//            val year = Integer.parseInt(eventDate[0])
//            val month = Integer.parseInt(eventDate[1])
//            val day = Integer.parseInt(eventDate[2])
//            binding.cvCalendarView
//                .addDecorator(
//                    EventDecorator(
//                        Color.parseColor("#FF018786"),
//                        Collections.singleton(CalendarDay.from(year, month-1, day))))
//        }

        CheckDot(routeList)


        //여기 수정~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
        val date :CalendarDay = CalendarDay.today()
        for (i in 0 until routeList.size) {
            //println("여기까진출력")
            //date = routeList.get(i).date.
            Log.d("dddd","${i} : ${date}")
            //binding.cvCalendarView.addDecorator(EventDecorator(Collections.singleton(date)))


            //binding.cvCalendarView.addDecorator(EventDecorator())
//            if (routeList[i].date?.get(Calendar.MONTH) != null) {
//                println("${routeList[i].date}")
//                //binding.cvCalendarView.addDecorator(clickDecorator())
//                //binding.cvCalendarView.addDecorator(EventDecorator(Collections.singleton(date)))
//            }
        }



// [2] 특정 날짜 선택
        binding.cvCalendarView.setOnDateChangedListener { widget, date, selected ->
            // 2-1 디데이 표시
            // 선택한 날짜 저장
            val dates: String
            if (date.month + 1 < 10) {
                dates = "${date.year}0${date.month + 1}${date.day}"
            } else
                dates = "${date.year}${date.month + 1}${date.day}"
            println("${date.year}/${date.month + 1}/${date.day}")

            // 디데이 계산
            val dday = dDay(dates)

            binding.tvDDay.setText("+♥${dday}")
            binding.tvDDay.visibility = View.VISIBLE

            // 2-2 해당 날짜에 루트가 존재하면 루트리스트(제목) 바인딩해주기
            /*
                if(존재){
                    리사이클러뷰 어댑터에 넣기
                }

             */

            // 리사이클러뷰 띄워주기

            showrecycler(routeList, date)
//            binding.rvTitle.layoutManager =
//                LinearLayoutManager(this.context, LinearLayoutManager.VERTICAL, false)
//            binding.rvTitle.setHasFixedSize(true)   // 성능개선
//            binding.rvTitle.adapter = RouteAdapter(routeList)
        }

// [3] 달력 월 변경 (< / >)
        // if(3-1 달력 넘김 선택 (<))
        //     (현재-1) 달 기준으로 1-3 번 진행
//        binding.cvCalendarView.setOnMonthChangedListener { widget, date ->
//            binding.tvMonthchange.setText("${date.year}년 ${date.month +1}월")
//
//        }

        /*
            cal.setOnMonthChangedListener { widget, date ->  }
         */


        // if(3-2 달력 넘김 선택 (>))
        //      (현재+1) 달 기준으로 1-3 번 진행

        /*
            cal.setOnMonthChangedListener { widget, date ->  }
         */






//        binding.rvTitle.layoutManager = LinearLayoutManager(this.context, LinearLayoutManager.VERTICAL, false)
//        binding.rvTitle.setHasFixedSize(true)   // 성능개선
//        binding.rvTitle.adapter = RouteAdapter(routeList)
    }
}

