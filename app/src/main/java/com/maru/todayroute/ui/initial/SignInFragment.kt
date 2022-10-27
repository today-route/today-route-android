package com.maru.todayroute.ui.initial

import android.content.Intent
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
import com.maru.todayroute.ui.MainActivity
import com.maru.todayroute.util.BaseFragment
import kotlinx.coroutines.launch

class SignInFragment : BaseFragment<FragmentSignInBinding>(R.layout.fragment_sign_in) {

    private val viewModel by activityViewModels<InitialViewModel>()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


        setUpObserver()

        // TODO: 로그인화면 서버와 연동 끝나면 사라질 코드
        binding.btnTmp.setOnClickListener {
            val intent = Intent(requireActivity(), MainActivity::class.java)
            startActivity(intent)
            activity?.finish()
        }

//        lifecycleScope.launch {
//            viewModel.checkInitialProgress()
//        }
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
            println("dkssudgktpdy")
            error?.let { println(error.message) }
            println(token)
            if (error == null && token != null) {
                viewModel.setUserInfoFromKakaoApi()
//                findNavController().navigate(R.id.action_signInFragment_to_initialUserInfoFragment)
            }
        }

        // 카카오톡이 단말기에 있으면
        if (UserApiClient.instance.isKakaoTalkLoginAvailable(requireContext())) {
            UserApiClient.instance.loginWithKakaoTalk(requireContext()) { token, error ->
                if (error != null) {
                    println("aaaaaaaaaa")
                    // 로그인 취소
                    if (error is ClientError && error.reason == ClientErrorCause.Cancelled) {
                        println("dhofrhohdi")
                        return@loginWithKakaoTalk
                    }

                    UserApiClient.instance.loginWithKakaoAccount(requireContext(), callback = callback)
                } else if (token != null) {
                    println("?")
                    viewModel.setUserInfoFromKakaoApi()

//                    findNavController().navigate(R.id.action_signInFragment_to_initialUserInfoFragment)
                }
            }
        // 카카오톡이 단말기에 없으면
        } else {
            UserApiClient.instance.loginWithKakaoAccount(requireContext(), callback = callback)
        }
    }
}