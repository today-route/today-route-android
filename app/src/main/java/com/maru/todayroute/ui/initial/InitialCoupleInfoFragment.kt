package com.maru.todayroute.ui.initial

import android.os.Bundle
import android.view.View
import androidx.navigation.fragment.findNavController
import com.maru.todayroute.R
import com.maru.todayroute.databinding.FragmentInitialCoupleInfoBinding
import com.maru.todayroute.util.BaseFragment

class InitialCoupleInfoFragment : BaseFragment<FragmentInitialCoupleInfoBinding>(R.layout.fragment_initial_couple_info) {

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.btnNext.setOnClickListener {
            findNavController().navigate(R.id.action_initialCoupleInfoFragment_to_connectCoupleFragment)
        }
    }
}