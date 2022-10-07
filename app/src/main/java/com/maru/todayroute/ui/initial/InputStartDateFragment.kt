package com.maru.todayroute.ui.initial

import android.app.DatePickerDialog
import android.os.Bundle
import android.view.View
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import com.maru.todayroute.R
import com.maru.todayroute.databinding.FragmentInputStartDateBinding
import com.maru.todayroute.util.BaseFragment

class InputStartDateFragment : BaseFragment<FragmentInputStartDateBinding>(R.layout.fragment_input_start_date) {

    private val viewModel: InitialViewModel by activityViewModels()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupButtonClickListener()
    }

    private fun setupButtonClickListener() {
        val navController = findNavController()

        binding.btnBack.setOnClickListener {
            navController.popBackStack()
        }

        binding.btnInviteCouple.setOnClickListener {
            navController.navigate(R.id.action_inputStartDateFragment_to_inviteCoupleFragment)
        }

        binding.etStartDate.setOnClickListener {
            DatePickerDialog(requireContext(),
                { _, year, month, dayOfMonth ->
                    binding.etStartDate.setText("${year}-${month + 1}-${dayOfMonth}")
                    viewModel.setStartDate("${year}년 ${month + 1}월 ${dayOfMonth}일")
                },
                viewModel.year,
                viewModel.month,
                viewModel.date
            ).show()
        }
    }
}