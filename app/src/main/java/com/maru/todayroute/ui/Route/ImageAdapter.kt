package com.maru.todayroute.ui.Route

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView
import com.maru.todayroute.R
import com.maru.todayroute.databinding.FragmentRouteBinding


class ImageAdapter(val imageList: ArrayList<Images>, val binding: FragmentRouteBinding): RecyclerView.Adapter<ImageAdapter.CustomViewHolder>(){

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CustomViewHolder {

        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_route_image, parent, false)

        return CustomViewHolder(view).apply{

            itemView.setOnClickListener {
                val curPos: Int = adapterPosition
                val image: Images = imageList.get(curPos)
                println("루트아이디 : ${image.routeId}, 이미지번호 : ${image.id}\n")

                // 선택한 이미지 띄우기
                binding.ivClickRouteImage.setImageResource(image.url)
                binding.ivClickRouteImage.visibility = View.VISIBLE

            }
        }

    }

    override fun onBindViewHolder(holder: CustomViewHolder, position: Int) {
        holder.image.setImageResource(imageList.get(position).url)
    }

    override fun getItemCount(): Int {
        return imageList.size
    }



    class CustomViewHolder(itemview: View): RecyclerView.ViewHolder(itemview) {
        val image = itemview.findViewById<ImageView>(R.id.iv_route_image)
    }

}