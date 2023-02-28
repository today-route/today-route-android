package com.maru.todayroute.ui.mypage

import android.os.Bundle
import android.view.View
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
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
        setupButtonClickListener()
    }

    private fun fetchMainData() {
        activityViewModel.fetchMainData()
    }


    private fun setupButtonClickListener() {
        val editUserButton = listOf(binding.btnBoyEdit, binding.btnGirlEdit)
        editUserButton.forEach { button ->
            button.setOnClickListener {
                findNavController().navigate(
                    MyPageFragmentDirections.actionMyPageFragmentToEditUserFragment(
                        activityViewModel.user.value!!
                    )
                )
            }
        }

        val editStartDateButton = listOf(binding.tvStartDate, binding.vEditStartDate)
        editStartDateButton.forEach { button ->
            button.setOnClickListener {
                findNavController().navigate(
                    MyPageFragmentDirections.actionMyPageFragmentToEditCoupleStartDateFragment(
                        activityViewModel.coupleInfo.value!!.startDate
                    )
                )
            }
        }

        binding.btnSettings.setOnClickListener {
            findNavController().navigate(R.id.action_my_page_fragment_to_settingsFragment)
        }
    }
}