package com.maru.todayroute.ui.Route

import android.os.Bundle
import android.view.View
import androidx.appcompat.view.menu.MenuView.ItemView
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
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

//data class Images(val id: Int,
//                  val url: String,
//                  val routeId: Int
//){
//}

val imageList = arrayListOf<Images>(
    Images(0,R.drawable.hongdae, 0),
    Images(1,R.drawable.linefriends, 0),
    Images(2,R.drawable.lamb,0),
    Images(3,R.drawable.hongdae, 0),
    Images(4,R.drawable.linefriends, 0),
    Images(5,R.drawable.lamb,0),
    Images(6,R.drawable.hongdae, 0),
    Images(7,R.drawable.linefriends, 0),
    Images(8,R.drawable.lamb,0),
    Images(9,R.drawable.hongdae1,9),
    Images(10,R.drawable.lamb,9),
    Images(11,R.drawable.linefriends,9),
    Images(12,R.drawable.lamb,9),
    Images(13,R.drawable.cafe,9),
    Images(14,R.drawable.lamb,9),
    Images(15,R.drawable.linefriends,9),
    Images(16,R.drawable.hongdae1,9),
    Images(17,R.drawable.linefriends,10),
    Images(18,R.drawable.lamb,10),
    Images(19,R.drawable.linefriends,10),
    Images(20,R.drawable.lamb,10),
    Images(21,R.drawable.linefriends,10),
    Images(22,R.drawable.lamb,10),
    Images(23,R.drawable.linefriends,10),
    Images(24,R.drawable.lamb,10),
)


class RouteFragment : BaseFragment<FragmentRouteBinding>(R.layout.fragment_route) {

    private val args: RouteFragmentArgs by navArgs()

    fun dateFormat(route: Route) : List<String>{

        val eventDate = route.date.split("-")
        return eventDate
    }

    fun showImage(imageList: ArrayList<Images>, routeId: Int){
        val matchImageList: ArrayList<Images> = ArrayList<Images>()

        for(i in imageList.indices){
            if(imageList.get(i).routeId == routeId)
                matchImageList.add(imageList[i])
        }
        // 사진 리스트 중 첫 아이콘 -> 루트
        val showRoute = Images(-1, R.drawable.route_image, routeId)
        matchImageList.add(0, showRoute)

        binding.rvRouteImage.layoutManager = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
        binding.rvRouteImage.setHasFixedSize(true)
        binding.rvRouteImage.adapter = ImageAdapter(matchImageList, binding)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.apply{
            val route: Route = routeList[args.routeId]
            val dates: List<String> = dateFormat(route)

            binding.tvRouteDateTop.text = "${dates.get(0)}년 ${dates.get(1)}월 ${dates.get(2)}일"  // 루트 작성 날짜
            binding.tvRouteTitle.text = route.title         // 제목
            binding.tvLocation.text = route.location        // 위치

            //binding.ivRoute = route.지도                   // 루트
            binding.ivRoute.setImageResource(R.drawable.route_image)        // 실제로는 mapView

            showImage(imageList, route.id)                  // 등록한 사진 리사이클러뷰 나열

            binding.etInsertContent.text = route.content    // 본문내용
        }

    }
}