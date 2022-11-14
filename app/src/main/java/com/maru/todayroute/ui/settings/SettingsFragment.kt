package com.maru.todayroute.ui.settings

import android.os.Bundle
import android.view.View
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.maru.todayroute.R
import com.maru.todayroute.databinding.FragmentSettingsBinding
import com.maru.todayroute.util.BaseFragment

class SettingsFragment : BaseFragment<FragmentSettingsBinding>(R.layout.fragment_settings) {

    private val viewModel: SettingsViewModel by viewModels()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupButtonClickListener()
    }

    private fun setupButtonClickListener() {
        with (binding) {
            btnBack.setOnClickListener {
                findNavController().popBackStack()
            }
        }
    }
}