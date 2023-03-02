package com.maru.todayroute.util

import androidx.databinding.BindingAdapter
import com.bumptech.glide.Glide
import com.bumptech.glide.request.target.Target
import com.google.android.material.imageview.ShapeableImageView
import com.maru.todayroute.R

@BindingAdapter("app:imageUrl")
fun bindProfileImage(imageView: ShapeableImageView, imageUrl: String?) {
    if (imageUrl != null) {
        Glide.with(imageView)
            .load(imageUrl)
            .placeholder(R.drawable.ic_circle_user_profile)
            .override(Target.SIZE_ORIGINAL)
            .into(imageView)
        imageView.clipToOutline = true
    }
}