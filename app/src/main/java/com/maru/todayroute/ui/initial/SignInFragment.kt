package com.maru.todayroute.ui.initial

import android.os.Bundle
import android.view.View
import androidx.navigation.fragment.findNavController
import com.kakao.sdk.auth.AuthApiClient
import com.kakao.sdk.auth.model.OAuthToken
import com.kakao.sdk.common.model.ClientError
import com.kakao.sdk.common.model.ClientErrorCause
import com.kakao.sdk.common.model.KakaoSdkError
import com.kakao.sdk.user.UserApiClient
import com.maru.todayroute.R
import com.maru.todayroute.databinding.FragmentSignInBinding
import com.maru.todayroute.util.BaseFragment

class SignInFragment : BaseFragment<FragmentSignInBinding>(R.layout.fragment_sign_in) {

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        if (AuthApiClient.instance.hasToken()) {
            UserApiClient.instance.accessTokenInfo { _, error ->
                if (!(error != null && error is KakaoSdkError && error.isInvalidTokenError())) {
                    // TODO: '초기 정보 입력'여부 확인 후 InitialProfileActivity로 갈 건지, MainActivity로 갈 건지 결정
                }
            }
        }

        binding.btnKakaoLogin.setOnClickListener {
            signIn()
        }
    }

    private fun signIn() {
        val callback: (OAuthToken?, Throwable?) -> Unit = { token, error ->
            if (error == null && token != null) {
                findNavController().navigate(R.id.action_signInFragment_to_initialUserInfoFragment)
            }
        }

        if (UserApiClient.instance.isKakaoTalkLoginAvailable(requireContext())) {
            UserApiClient.instance.loginWithKakaoTalk(requireContext()) { token, error ->
                if (error != null) {

                    // 로그인 취소
                    if (error is ClientError && error.reason == ClientErrorCause.Cancelled) {
                        return@loginWithKakaoTalk
                    }

                    UserApiClient.instance.loginWithKakaoAccount(requireContext(), callback = callback)
                } else if (token != null) {
                    findNavController().navigate(R.id.action_signInFragment_to_initialUserInfoFragment)
                }
            }
        } else {
            UserApiClient.instance.loginWithKakaoAccount(requireContext(), callback = callback)
        }
    }
}