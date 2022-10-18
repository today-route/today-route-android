package com.maru.todayroute.ui.calendar

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.maru.todayroute.R

class RouteAdapter(val routeList: ArrayList<Route>): RecyclerView.Adapter<RouteAdapter.CustomViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CustomViewHolder {
        // item_route_diary.xml 을 view에 가져옴
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_route_diary, parent, false)
        return CustomViewHolder(view)
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