package com.maru.todayroute.ui.addroute

import android.net.Uri
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.maru.todayroute.R
import com.maru.todayroute.databinding.ItemPhotoInAddRouteBinding

class PhotoListAdapter(private val removePhoto: (Int) -> Unit) : ListAdapter<Uri, PhotoListAdapter.PhotoViewHolder>(DIFF_CALLBACK) {

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

        fun bind(uri: Uri) {
            with (binding.ivPhoto) {
                setImageURI(uri)
            }
        }
    }

    companion object {
        val DIFF_CALLBACK: DiffUtil.ItemCallback<Uri> = object : DiffUtil.ItemCallback<Uri>() {
            override fun areItemsTheSame(oldItem: Uri, newItem: Uri): Boolean =
                oldItem === newItem

            override fun areContentsTheSame(oldItem: Uri, newItem: Uri): Boolean =
                oldItem == newItem
        }
    }
}