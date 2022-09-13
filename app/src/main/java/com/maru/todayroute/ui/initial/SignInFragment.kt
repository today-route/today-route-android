package com.maru.todayroute.ui.initial

import android.os.Bundle
import android.view.View
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.kakao.sdk.auth.model.OAuthToken
import com.kakao.sdk.common.model.ClientError
import com.kakao.sdk.common.model.ClientErrorCause
import com.kakao.sdk.user.UserApiClient
import com.maru.todayroute.R
import com.maru.todayroute.databinding.FragmentSignInBinding
import com.maru.todayroute.util.BaseFragment
import kotlinx.coroutines.launch

class SignInFragment : BaseFragment<FragmentSignInBinding>(R.layout.fragment_sign_in) {

    private val viewModel by activityViewModels<InitialViewModel>()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


        setUpObserver()
        lifecycleScope.launch {
            viewModel.checkUserInfo()
        }
        binding.btnKakaoLogin.setOnClickListener {
            signIn()
        }
    }

    private fun setUpObserver() {
        viewModel.moveToConnectCoupleFragment.observe(viewLifecycleOwner) {
            findNavController().navigate(R.id.action_signInFragment_to_connectCoupleFragment)
        }
        viewModel.moveToInitialUserInfoFragment.observe(viewLifecycleOwner) {
            findNavController().navigate(R.id.action_signInFragment_to_initialUserInfoFragment)
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
                    viewModel.setUserInfoFromKakaoApi()
                }
            }
        } else {
            UserApiClient.instance.loginWithKakaoAccount(requireContext(), callback = callback)
        }
    }
}