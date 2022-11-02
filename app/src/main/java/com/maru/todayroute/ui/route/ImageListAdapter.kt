package com.maru.todayroute.ui.route

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.DiffUtil.ItemCallback
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.maru.todayroute.R
import com.maru.todayroute.databinding.FragmentRouteBinding
import com.maru.todayroute.databinding.ItemRouteImageBinding

class ImageListAdapter(private val fragmentBinding: FragmentRouteBinding) : ListAdapter<Int, ImageListAdapter.RoutePhotoViewHolder>(DIFF_CALLBACK) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RoutePhotoViewHolder =
        RoutePhotoViewHolder(DataBindingUtil.inflate(LayoutInflater.from(parent.context), R.layout.item_route_image, parent, false))

    override fun onBindViewHolder(holder: RoutePhotoViewHolder, position: Int) {
        when (position) {
            0 -> {
                holder.bind(getItem(0), false)
            }
            else -> {
                holder.bind(getItem(position), true)
            }
        }
    }

    inner class RoutePhotoViewHolder(private val binding: ItemRouteImageBinding) : RecyclerView.ViewHolder(binding.root) {

        private lateinit var clickListener: () -> Unit

        init {
            binding.ivRouteImage.setOnClickListener {
                clickListener.invoke()
            }
        }

        fun bind(url: Int, isImage: Boolean) { // TODO: server와 연결했을 때 url을 String으로 변경하기
            binding.ivRouteImage.setImageResource(url)
            if (isImage) {
                clickListener = {
                    with (fragmentBinding.ivClickRouteImage) {
                        isVisible = true
                        setImageResource(url)
                    }
                }
            } else {
                clickListener = {
                    fragmentBinding.ivClickRouteImage.isVisible = false
                }
            }
        }
    }

    companion object {
        val DIFF_CALLBACK = object : ItemCallback<Int>() {
            override fun areItemsTheSame(oldItem: Int, newItem: Int): Boolean =
                oldItem == newItem

            override fun areContentsTheSame(oldItem: Int, newItem: Int): Boolean =
                oldItem == newItem
        }
    }
}