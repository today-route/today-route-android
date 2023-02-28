package com.maru.todayroute.ui.customview

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import androidx.constraintlayout.widget.ConstraintLayout
import com.maru.todayroute.databinding.CustomviewCoupleInfoBinding
import com.maru.todayroute.util.bindProfileImage

class CoupleInfoView(context: Context, attrs: AttributeSet) : ConstraintLayout(context, attrs) {

    val binding: CustomviewCoupleInfoBinding

    init {
        val layoutInflater = LayoutInflater.from(context)
        binding = CustomviewCoupleInfoBinding.inflate(layoutInflater, this, false)
        addView(binding.root)
    }

    fun setDDay(dDay: String?) {
        dDay?.let {
            binding.tvDDay.text = it
        }
    }

    fun setBoyNickname(boyNickname: String?) {
        boyNickname?.let {
            binding.tvBoyNickname.text = it
        }
    }

    fun setGirlNickname(girlNickname: String?) {
        girlNickname?.let {
            binding.tvGirlNickname.text = it
        }
    }

    fun setBoyProfileImageUrl(imageUrl: String?) {
        imageUrl?.let { bindProfileImage(binding.ivBoyProfile, it) }
    }

    fun setGirlProfileImageUrl(imageUrl: String?) {
        imageUrl?.let { bindProfileImage(binding.ivGirlProfile, it) }
    }
}