package com.maru.todayroute.ui.initial

import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import com.maru.data.model.Gender
import com.maru.todayroute.R
import com.maru.todayroute.databinding.FragmentInitialUserInfoBinding
import com.maru.todayroute.util.BaseFragment
import com.maru.todayroute.util.Utils

class InitialUserInfoFragment :
    BaseFragment<FragmentInitialUserInfoBinding>(R.layout.fragment_initial_user_info) {

    private val viewModel by activityViewModels<InitialViewModel>()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupButtonClickListener()
        setupObserver()
    }

    private fun setupButtonClickListener() {
        binding.rgGender.setOnCheckedChangeListener { _, id ->
            when (id) {
                R.id.rb_female -> viewModel.setUserGender(Gender.F)
                R.id.rb_male -> viewModel.setUserGender(Gender.M)
            }
        }

        binding.btnNext.setOnClickListener {
            registerNewUser()
        }

        binding.etUserBirthday.setOnClickListener {
            viewModel.editDateButtonClicked()
        }
    }

    private fun setupObserver() {
        viewModel.moveToConnectCoupleFragment.observe(viewLifecycleOwner) {
            findNavController().navigate(R.id.action_initialUserInfoFragment_to_connectCoupleFragment)
        }

        viewModel.showDatePickerDialog.observe(viewLifecycleOwner) { d ->
            Utils.showDatePicker(requireContext(), binding.etUserBirthday, d, viewModel::setDate)
        }

        viewModel.showToastMessage.observe(viewLifecycleOwner) { messageId ->
            Toast.makeText(context, getString(messageId), Toast.LENGTH_SHORT).show()
        }
    }

    private fun registerNewUser() {
        viewModel.registerNewUser(binding.etUserBirthday.text.toString())
    }
}