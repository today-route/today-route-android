package com.maru.todayroute.ui.Route

import android.os.Parcel
import android.os.Parcelable
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView
import com.maru.todayroute.R



class ImageAdapter(val imageList: ArrayList<Images>): RecyclerView.Adapter<ImageAdapter.CustomViewHolder>(){


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CustomViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_route_image, parent, false)
        return CustomViewHolder(view).apply{
            itemView.setOnClickListener {

            }
        }

    }

    override fun onBindViewHolder(holder: CustomViewHolder, position: Int) {
        holder.image.setImageResource(imageList.get(position).url)
    //holder.title.text = routeList.get(position).title
    }

    override fun getItemCount(): Int {
        return imageList.size
    }



    class CustomViewHolder(itemview: View): RecyclerView.ViewHolder(itemview) {
        val image = itemview.findViewById<ImageView>(R.id.iv_route_image)
    }


}