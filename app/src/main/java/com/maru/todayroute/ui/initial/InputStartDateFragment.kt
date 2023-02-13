package com.maru.todayroute.ui.initial

import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import com.maru.todayroute.R
import com.maru.todayroute.databinding.FragmentInputStartDateBinding
import com.maru.todayroute.util.BaseFragment
import com.maru.todayroute.util.Utils.showDatePicker

class InputStartDateFragment : BaseFragment<FragmentInputStartDateBinding>(R.layout.fragment_input_start_date) {

    private val viewModel: InitialViewModel by activityViewModels()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel.initDate()
        setupObserver()
        setupButtonClickListener()
    }

    private fun setupObserver() {
        viewModel.showToastMessage.observe(viewLifecycleOwner) { messageId ->
            Toast.makeText(requireContext(), getString(messageId), Toast.LENGTH_SHORT).show()
        }

        viewModel.showDatePickerDialog.observe(viewLifecycleOwner) { d ->
            showDatePicker(requireContext(), binding.etStartDate, d, viewModel::setDate)
        }
    }

    private fun setupButtonClickListener() {
        val navController = findNavController()

        binding.btnBack.setOnClickListener {
            navController.popBackStack()
        }

        binding.btnStart.setOnClickListener {
            viewModel.connectCoupleByCode(binding.etStartDate.text.toString())
        }

        binding.etStartDate.setOnClickListener {
            viewModel.editDateButtonClicked()
        }
    }
}