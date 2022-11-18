package com.maru.todayroute.ui.settings

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.core.content.ContentProviderCompat.requireContext
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.maru.todayroute.R
import com.maru.todayroute.databinding.FragmentSettingsBinding
import com.maru.todayroute.ui.initial.InitialActivity
import com.maru.todayroute.util.BaseFragment
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

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
            btnBreakUp.setOnClickListener {
                showAlertDialog("커플 연결 끊기", "정말로 커플 연결을 끊으시겠어요?", "끊기") {
                    lifecycleScope.launch {
                        viewModel.breakUp()
                    }
                }
            }
            btnSignOut.setOnClickListener {
                showAlertDialog("로그아웃", "정말로 로그아웃 하시겠어요?", "로그아웃") {
                    viewModel.signOut()
                }
            }
            btnDeleteUser.setOnClickListener {
                showAlertDialog("회원탈퇴", "정말로 오늘의 길을 탈퇴하시겠어요?", "탈퇴") {
                    lifecycleScope.launch {
                        viewModel.deleteUser()
                    }
                }
            }
        }
    }

    private fun showAlertDialog(title: String, message: String, positive: String, positiveButtonClicked: () -> Unit) {
        val alertDialogBuilder = AlertDialog.Builder(requireActivity())
            .setTitle(title)
            .setMessage(message)
            .setPositiveButton(positive) { _, _ ->
                positiveButtonClicked.invoke()
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