package com.maru.todayroute.ui.initial

import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.maru.todayroute.R
import com.maru.todayroute.databinding.FragmentInputInviteCodeBinding
import com.maru.todayroute.util.BaseFragment
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class InputInviteCodeFragment : BaseFragment<FragmentInputInviteCodeBinding>(R.layout.fragment_input_invite_code) {

    private val viewModel by activityViewModels<InitialViewModel>()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupButtonClickListener()
        binding.viewModel = viewModel
    }

    private fun setupButtonClickListener() {
        binding.btnBack.setOnClickListener {
            findNavController().popBackStack()
        }

        binding.btnNext.setOnClickListener {
            viewModel.setInviteCode(binding.etInviteCode.text.toString())
            findNavController().navigate(R.id.action_inputInviteCodeFragment_to_inputStartDateFragment)
        }
    }

}