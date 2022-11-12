package com.maru.todayroute.ui.mypage

import android.app.DatePickerDialog
import android.os.Bundle
import android.view.View
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.maru.todayroute.R
import com.maru.todayroute.databinding.FragmentEditCoupleStartDateBinding
import com.maru.todayroute.util.BaseFragment
import com.maru.todayroute.util.Utils.convertSingleToDoubleDigit
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class EditCoupleStartDateFragment :
    BaseFragment<FragmentEditCoupleStartDateBinding>(R.layout.fragment_edit_couple_start_date) {

    private val viewModel: EditCoupleStartDateViewModel by viewModels()
    private val args: EditCoupleStartDateFragmentArgs by navArgs()
    private val startDate by lazy {
        args.startDate.substring(0 until 10).split("-").map { it.toInt() }.toMutableList()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.viewModel = viewModel
        viewModel.setStartDate(args.startDate)
        startDate[1] -= 1
        setupButtonClickListener()
    }

    private fun setupButtonClickListener() {
        with(binding) {
            btnBack.setOnClickListener {
                findNavController().popBackStack()
            }
            etStartDate.setOnClickListener {
                DatePickerDialog(
                    requireContext(),
                    { _, year, m, d ->
                        val month = (m + 1).toString().convertSingleToDoubleDigit()
                        val dayOfMonth = d.toString().convertSingleToDoubleDigit()
                        binding.etStartDate.setText("${year}-${month}-${dayOfMonth}")
                        startDate[0] = year
                        startDate[1] = m
                        startDate[2] = d
                    },
                    startDate[0],
                    startDate[1],
                    startDate[2]
                ).show()
            }
        }
    }
}