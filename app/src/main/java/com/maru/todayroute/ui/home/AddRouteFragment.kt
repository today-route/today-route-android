package com.maru.todayroute.ui.home

import android.os.Bundle
import android.view.View
import androidx.navigation.fragment.findNavController
import com.maru.todayroute.R
import com.maru.todayroute.databinding.FragmentAddRouteBinding
import com.maru.todayroute.util.BaseFragment
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class AddRouteFragment : BaseFragment<FragmentAddRouteBinding>(R.layout.fragment_add_route) {

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupButtonClickListener()
    }

    private fun setupButtonClickListener() {
        binding.btnBack.setOnClickListener {
            findNavController().popBackStack()
        }
    }
}