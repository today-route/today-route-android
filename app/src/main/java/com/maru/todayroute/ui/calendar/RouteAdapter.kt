package com.maru.todayroute.ui.calendar

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.navigation.Navigation.findNavController
import androidx.navigation.findNavController
import androidx.recyclerview.widget.RecyclerView
import com.maru.todayroute.R
import com.maru.todayroute.ui.Route.RouteFragment

class RouteAdapter(val routeList: ArrayList<Route>): RecyclerView.Adapter<RouteAdapter.CustomViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CustomViewHolder {
        // item_route_diary.xml 을 view에 가져옴
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_route_diary, parent, false)
        return CustomViewHolder(view).apply{
            itemView.setOnClickListener {
                val curPos: Int = adapterPosition
                val route: Route = routeList.get(curPos)
                val routeId: Int = route.id

                view.findNavController().navigate(CalendarFragmentDirections.actionCalendarFragmentToRouteFeragment(routeId))


                //it.findNavController().navigate(R.id.action_homeFragment_to_bookmarkFragment)
                //Toast.makeText(parent.context, "사용자 id : ${route.id}\n 제목 : ${route.title}\n" , Toast.LENGTH_SHORT).show()

                //var action = FragmentRouteDirection.action
                //var action =  MainFragmentDirections.actionMainToThirdFragment()
                //findNavController().navigate(action)

                //val navAction = RouteFragment.moveToNewFragment("Title Data")
                //val navAction = TestFragmentDirections.moveToNewFragment("Title Data")
                //findNavController().navigate(navAction)

            }
        }
    }

    override fun onBindViewHolder(holder: CustomViewHolder, position: Int) {
        holder.title.text = routeList.get(position).title
        Log.d("number","${position}")
    }

    override fun getItemCount(): Int {
        return routeList.size
    }

    class CustomViewHolder(itemview: View): RecyclerView.ViewHolder(itemview){
        val title = itemview.findViewById<TextView>(R.id.tv_route_title)
    }
}