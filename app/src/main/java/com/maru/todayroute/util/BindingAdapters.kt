package com.maru.todayroute.util

import android.widget.ImageView
import androidx.databinding.BindingAdapter
import androidx.lifecycle.MutableLiveData
import com.bumptech.glide.Glide

@BindingAdapter("app:imageUrl")
fun bindProfileImage(imageView: ImageView, imageUrl: MutableLiveData<String>) {
    Glide.with(imageView)
        .load(imageUrl.value)
        .into(imageView)
    imageView.clipToOutline = true
}