package com.maru.todayroute.ui.initial

import android.net.Uri
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import com.google.firebase.dynamiclinks.DynamicLink
import com.google.firebase.dynamiclinks.FirebaseDynamicLinks
import com.kakao.sdk.share.ShareClient
import com.maru.todayroute.R
import com.maru.todayroute.databinding.FragmentInviteCoupleBinding
import com.maru.todayroute.util.BaseFragment

class InviteCoupleFragment :
    BaseFragment<FragmentInviteCoupleBinding>(R.layout.fragment_invite_couple) {

    private val viewModel by activityViewModels<InitialViewModel>()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupButtonClickListener()
        setupObserver()
        binding.viewModel = viewModel
    }

    private fun setupButtonClickListener() {
        binding.btnInviteCouple.setOnClickListener {
            invite()
        }
        binding.btnBack.setOnClickListener {
            findNavController().popBackStack()
        }
        binding.btnStart.setOnClickListener {
            viewModel.tryStart()
        }
    }

    private fun setupObserver() {
        viewModel.showToastMessage.observe(viewLifecycleOwner) { messageId ->
            Toast.makeText(requireContext(), getString(messageId), Toast.LENGTH_SHORT).show()
        }
    }

    private fun invite() {
        val dynamicLink = FirebaseDynamicLinks.getInstance().createDynamicLink()
            .setLink(Uri.parse("${TODAY_ROUTE_URI_PREFIX}/invite?code=${viewModel.code}"))
            .setDomainUriPrefix(TODAY_ROUTE_URI_PREFIX)
            .setAndroidParameters(DynamicLink.AndroidParameters.Builder().build())
            .buildShortDynamicLink()

        dynamicLink.addOnSuccessListener { task ->
            val inviteLink = task.shortLink!!
            sendInviteLink(inviteLink)
        }
    }

    private fun sendInviteLink(inviteLink: Uri) {
        if (ShareClient.instance.isKakaoTalkSharingAvailable(requireContext())) {
            val textTemplate = viewModel.makeInviteTextTemplate(
                getString(R.string.kakao_message_content_text),
                inviteLink,
                getString(R.string.kakao_message_button_text)
            )

            ShareClient.instance.shareDefault(requireContext(), textTemplate) { sharingResult, error ->
                if (error != null) {
                    Toast.makeText(requireContext(), getString(R.string.warning_kakao_share_fail), Toast.LENGTH_SHORT).show()
                } else if (sharingResult != null) {
                    startActivity(sharingResult.intent)
                }
            }
        }
    }

    companion object {
        const val TODAY_ROUTE_URI_PREFIX = "https://todayroute.page.link"
    }
}