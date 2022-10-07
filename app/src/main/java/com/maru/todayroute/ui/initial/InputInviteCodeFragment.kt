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
        setupObserver()
        binding.viewModel = viewModel
    }

    private fun setupButtonClickListener() {
        binding.btnBack.setOnClickListener {
            showAlertDialog()
        }

        binding.btnConnect.setOnClickListener {
            lifecycleScope.launch {
                viewModel.connectCoupleByCode(binding.etInviteCode.text.toString())
            }
        }
    }

    private fun setupObserver() {
        viewModel.showCoupleNameCheckDialog.observe(viewLifecycleOwner) { name ->
            MaterialAlertDialogBuilder(requireContext())
                .setTitle("${name}님과 연결하시겠습니까?")
                .setNegativeButton("아니요") { dialog, _ ->
                    dialog.dismiss()
                }
                .setPositiveButton("네") { dialog, _ ->
                    lifecycleScope.launch {
                        viewModel.registerNewCouple()
                    }
                    dialog.dismiss()
                }
                .show()
        }
    }

    private fun showAlertDialog() {
        MaterialAlertDialogBuilder(requireContext())
            .setTitle("정말 뒤로 가시겠습니까?")
            .setMessage("지금까지 입력하신 초대코드가 사라집니다.")
            .setNegativeButton("아니요") { dialog, _ ->
                dialog.dismiss()
            }
            .setPositiveButton("네") { dialog, _ ->
                findNavController().popBackStack()
                dialog.dismiss()
            }
            .show()
    }
}