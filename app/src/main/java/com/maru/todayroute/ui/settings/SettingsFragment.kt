package com.maru.todayroute.ui.settings

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.maru.todayroute.R
import com.maru.todayroute.databinding.FragmentSettingsBinding
import com.maru.todayroute.ui.initial.InitialActivity
import com.maru.todayroute.util.BaseFragment
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class SettingsFragment : BaseFragment<FragmentSettingsBinding>(R.layout.fragment_settings) {

    private val viewModel: SettingsViewModel by viewModels()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupButtonClickListener()
        setupObserver()
    }

    private fun setupButtonClickListener() {
        with(binding) {
            btnBack.setOnClickListener {
                findNavController().popBackStack()
            }

            btnSignOut.setOnClickListener {
                showAlertDialog()
            }
        }
    }

    private fun showAlertDialog() {
        val alertDialogBuilder = AlertDialog.Builder(requireActivity())
            .setTitle("로그아웃")
            .setMessage("정말로 로그아웃 하시겠어요?")
            .setPositiveButton("로그아웃") { _, _ ->
                viewModel.signOut()
            }
            .setNegativeButton("아니요") { dialog, _ ->
                dialog.dismiss()
            }
        alertDialogBuilder.show()
    }

    private fun setupObserver() {
        with (viewModel) {
            moveToInitialActivity.observe(viewLifecycleOwner) {
                this@SettingsFragment.moveToInitialActivity()
                requireActivity().finish()
            }
            showToastMessage.observe(viewLifecycleOwner) { message ->
                Toast.makeText(requireContext(), message, Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun moveToInitialActivity() {
        val intent = Intent(requireContext(), InitialActivity::class.java)
        startActivity(intent)
    }
}