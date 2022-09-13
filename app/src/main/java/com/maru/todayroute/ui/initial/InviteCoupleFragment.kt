package com.maru.todayroute.ui.initial

import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import com.google.firebase.dynamiclinks.DynamicLink
import com.google.firebase.dynamiclinks.FirebaseDynamicLinks
import com.kakao.sdk.share.ShareClient
import com.kakao.sdk.template.model.Link
import com.kakao.sdk.template.model.TextTemplate
import com.maru.todayroute.R
import com.maru.todayroute.databinding.FragmentInviteCoupleBinding
import com.maru.todayroute.util.BaseFragment

class InviteCoupleFragment :
    BaseFragment<FragmentInviteCoupleBinding>(R.layout.fragment_invite_couple) {

    private val viewModel by activityViewModels<InitialViewModel>()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupButtonClickListener()
    }

    private fun setupButtonClickListener() {
        binding.btnInviteCouple.setOnClickListener {
            invite()
        }
        binding.btnBack.setOnClickListener {
            findNavController().popBackStack()
        }
    }

    private fun invite() {
        val dynamicLink = FirebaseDynamicLinks.getInstance().createDynamicLink()
            .setLink(Uri.parse("https://todayroute.page.link/invite?code=임시초대코드"))
            .setDomainUriPrefix("https://todayroute.page.link")
            .setAndroidParameters(DynamicLink.AndroidParameters.Builder().build())
            .buildShortDynamicLink()

        dynamicLink.addOnSuccessListener { task ->
            val inviteLink = task.shortLink!!
            sendInviteLink(inviteLink)
        }
    }

    private fun sendInviteLink(inviteLink: Uri) {
        if (ShareClient.instance.isKakaoTalkSharingAvailable(requireContext())) {
            val textTemplate = TextTemplate(
                text = """
                    OOO님이 함께 오늘의 길을 만들고 싶어해요!
                    초대 코드 : 임시초대코드
                """.trimIndent(),
                link = Link(mobileWebUrl = inviteLink.toString()),
                buttonTitle = "초대 코드 입력하기"
            )

            ShareClient.instance.shareDefault(requireContext(), textTemplate) { sharingResult, error ->
                if (error != null) {
                    Log.e("Kakao", "카카오톡 공유 실패", error)
                } else if (sharingResult != null) {
                    startActivity(sharingResult.intent)
                }
            }
        }
    }
}