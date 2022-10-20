package com.maru.todayroute.ui.initial

import android.app.DatePickerDialog
import android.os.Bundle
import android.view.View
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import com.maru.todayroute.R
import com.maru.todayroute.databinding.FragmentInputStartDateBinding
import com.maru.todayroute.util.BaseFragment
import com.maru.todayroute.util.Utils.convertSingleToDoubleDigit
import java.util.*

class InputStartDateFragment : BaseFragment<FragmentInputStartDateBinding>(R.layout.fragment_input_start_date) {

    private val viewModel: InitialViewModel by activityViewModels()

    private val calendar: Calendar = GregorianCalendar()
    private var year = calendar.get(Calendar.YEAR)
    private var month = calendar.get(Calendar.MONTH)
    private var date = calendar.get(Calendar.DATE)

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
                { _, year, m, d ->
                    val month = (m + 1).toString().convertSingleToDoubleDigit()
                    val dayOfMonth = d.toString().convertSingleToDoubleDigit()
                    binding.etStartDate.setText("${year}-${month}-${dayOfMonth}")
                    viewModel.setStartDate("${year}-${month}-${dayOfMonth}")
                    this.year = year
                    this.month = m
                    date = d
                },
                year,
                month,
                date
            ).show()
        }
    }
}