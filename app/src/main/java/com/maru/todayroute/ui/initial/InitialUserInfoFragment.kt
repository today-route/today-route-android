package com.maru.todayroute.ui.initial

import android.os.Bundle
import android.view.View
import androidx.navigation.fragment.findNavController
import com.maru.todayroute.R
import com.maru.todayroute.databinding.FragmentInitialUserInfoBinding
import com.maru.todayroute.util.BaseFragment

class InitialUserInfoFragment : BaseFragment<FragmentInitialUserInfoBinding>(R.layout.fragment_initial_user_info) {

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.btnNext.setOnClickListener {
            findNavController().navigate(R.id.action_initialUserInfoFragment_to_connectCoupleFragment)
        }
    }
}