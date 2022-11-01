package com.maru.todayroute.ui.route

import android.os.Bundle
import android.view.View
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import com.maru.data.model.Route
import com.maru.todayroute.R
import com.maru.todayroute.databinding.FragmentRouteBinding
import com.maru.todayroute.util.BaseFragment
import com.maru.todayroute.util.Dummy
import com.maru.todayroute.util.Dummy.routeList

//data class Images(val id: Int,
//                  val url: String,
//                  val routeId: Int
//){
//}


class RouteFragment : BaseFragment<FragmentRouteBinding>(R.layout.fragment_route) {

    private val args: RouteFragmentArgs by navArgs()

    fun dateFormat(route: Route) : List<String>{

        val eventDate = route.date.split("-")
        return eventDate
    }

    fun showImage(imageList: List<Images>, routeId: Int){
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
        setupButtonClickListener()

        binding.apply{
            val route: Route = routeList[args.routeId]
            val dates: List<String> = dateFormat(route)

            binding.tvRouteDateTop.text = "${dates.get(0)}년 ${dates.get(1)}월 ${dates.get(2)}일"  // 루트 작성 날짜
            binding.tvRouteTitle.text = route.title         // 제목
            binding.tvLocation.text = route.location        // 위치

            //binding.ivRoute = route.지도                   // 루트
            binding.ivRoute.setImageResource(R.drawable.route_image)        // 실제로는 mapView

            showImage(Dummy.imageList, route.id)                  // 등록한 사진 리사이클러뷰 나열

            binding.etInsertContent.text = route.content    // 본문내용
        }

    }

    private fun setupButtonClickListener() {
        binding.btnBack.setOnClickListener {
            findNavController().popBackStack()
        }
    }
}