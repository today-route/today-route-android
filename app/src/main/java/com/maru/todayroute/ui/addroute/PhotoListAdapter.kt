package com.maru.todayroute.ui.addroute

import android.graphics.Bitmap
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.maru.todayroute.R
import com.maru.todayroute.databinding.ItemPhotoInAddRouteBinding

class PhotoListAdapter(private val removePhoto: (Int) -> Unit) : ListAdapter<Bitmap, PhotoListAdapter.PhotoViewHolder>(DIFF_CALLBACK) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PhotoViewHolder {
        return PhotoViewHolder(DataBindingUtil.inflate(LayoutInflater.from(parent.context), R.layout.item_photo_in_add_route, parent, false))
    }

    override fun onBindViewHolder(holder: PhotoViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    inner class PhotoViewHolder(private val binding: ItemPhotoInAddRouteBinding): RecyclerView.ViewHolder(binding.root) {

        init {
            binding.btnRemove.setOnClickListener {
                removePhoto.invoke(adapterPosition)
            }
        }

        fun bind(bitmap: Bitmap) {
            with (binding.ivPhoto) {
                setImageBitmap(bitmap)
            }
        }
    }

    companion object {
        val DIFF_CALLBACK: DiffUtil.ItemCallback<Bitmap> = object : DiffUtil.ItemCallback<Bitmap>() {
            override fun areItemsTheSame(oldItem: Bitmap, newItem: Bitmap): Boolean =
                oldItem === newItem

            override fun areContentsTheSame(oldItem: Bitmap, newItem: Bitmap): Boolean =
                oldItem.sameAs(newItem)
        }
    }
}