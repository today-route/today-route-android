package com.maru.todayroute.ui.mypage

import android.os.Bundle
import android.view.View
import androidx.fragment.app.activityViewModels
import com.maru.todayroute.R
import com.maru.todayroute.databinding.FragmentMyPageBinding
import com.maru.todayroute.ui.MainViewModel
import com.maru.todayroute.util.BaseFragment
import com.maru.todayroute.util.Utils
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MyPageFragment : BaseFragment<FragmentMyPageBinding>(R.layout.fragment_my_page) {

    private val activityViewModel: MainViewModel by activityViewModels()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.activityViewModel = activityViewModel
        fetchMainData()
        setupObserver()
    }

    private fun fetchMainData() {
        activityViewModel.fetchMainData()
    }

    private fun setupObserver() {
        activityViewModel.coupleInfo.observe(viewLifecycleOwner) { coupleInfo ->
            binding.tvDDay.text = Utils.calculateDDay(coupleInfo.startDate)
        }
    }
}