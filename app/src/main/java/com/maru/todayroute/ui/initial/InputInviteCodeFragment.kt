package com.maru.todayroute.ui.initial

import android.annotation.SuppressLint
import android.os.Bundle
import android.util.Log
import android.view.KeyEvent
import android.view.View
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.maru.todayroute.R
import com.maru.todayroute.databinding.FragmentInputInviteCodeBinding
import com.maru.todayroute.util.BaseFragment
import com.maru.todayroute.util.hideKeyboard
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class InputInviteCodeFragment : BaseFragment<FragmentInputInviteCodeBinding>(R.layout.fragment_input_invite_code) {

    private val viewModel by activityViewModels<InitialViewModel>()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupButtonClickListener()
        setupKeyboardListener()
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

    @SuppressLint("ClickableViewAccessibility")
    private fun setupKeyboardListener() {
        binding.etInviteCode.setOnKeyListener { _, keyCode, event ->
            if ((event.action == KeyEvent.ACTION_DOWN) && (keyCode == KeyEvent.KEYCODE_ENTER)) {
                hideKeyboard(requireActivity())
                true
            }
            false
        }


        binding.layout.setOnTouchListener { view, event ->
            if (activity != null && activity?.currentFocus != null) {
                hideKeyboard(requireActivity())
                true
            }
            false
        }
    }
}