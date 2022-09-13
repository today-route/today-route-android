package com.maru.todayroute.ui.initial

import android.os.Bundle
import android.view.View
import androidx.navigation.fragment.findNavController
import com.maru.todayroute.R
import com.maru.todayroute.databinding.FragmentConnectCoupleBinding
import com.maru.todayroute.util.BaseFragment

class ConnectCoupleFragment : BaseFragment<FragmentConnectCoupleBinding>(R.layout.fragment_connect_couple) {

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupButtonClickListener()
    }

    private fun setupButtonClickListener() {
        val navController = findNavController()

        binding.btnInviteCouple.setOnClickListener {
            navController.navigate(R.id.action_connectCoupleFragment_to_inviteCoupleFragment)
        }

        binding.btnInputInviteCode.setOnClickListener {
            navController.navigate(R.id.action_connectCoupleFragment_to_inputInviteCodeFragment)
        }
    }
}