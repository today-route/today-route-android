package com.maru.todayroute.ui.route

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.DiffUtil.ItemCallback
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.maru.todayroute.R
import com.maru.todayroute.databinding.FragmentRouteBinding
import com.maru.todayroute.databinding.ItemRouteImageBinding

class ImageListAdapter(private val fragmentBinding: FragmentRouteBinding) : ListAdapter<String, ImageListAdapter.RoutePhotoViewHolder>(DIFF_CALLBACK) {

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

        fun bind(url: String, isImage: Boolean) {

            if (isImage) {
                Glide.with(binding.ivRouteImage).load(url).into(binding.ivRouteImage)
                clickListener = {
                    with (fragmentBinding.ivClickRouteImage) {
                        isVisible = true
                        Glide.with(this).load(url).into(this)
                    }
                }
            } else {
                binding.ivRouteImage.setImageResource(url.toInt())
                clickListener = {
                    fragmentBinding.ivClickRouteImage.isVisible = false
                }
            }
        }
    }

    companion object {
        val DIFF_CALLBACK = object : ItemCallback<String>() {
            override fun areItemsTheSame(oldItem: String, newItem: String): Boolean =
                oldItem == newItem

            override fun areContentsTheSame(oldItem: String, newItem: String): Boolean =
                oldItem == newItem
        }
    }
}