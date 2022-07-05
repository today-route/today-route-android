package com.maru.todayroute.ui.initial

import android.content.Intent
import android.os.Bundle
import com.kakao.sdk.auth.AuthApiClient
import com.kakao.sdk.auth.model.OAuthToken
import com.kakao.sdk.common.model.ClientError
import com.kakao.sdk.common.model.ClientErrorCause
import com.kakao.sdk.common.model.KakaoSdkError
import com.kakao.sdk.user.UserApiClient
import com.maru.todayroute.R
import com.maru.todayroute.databinding.ActivitySignInBinding
import com.maru.todayroute.util.BaseActivity

class SignInActivity : BaseActivity<ActivitySignInBinding>(R.layout.activity_sign_in) {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

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
                startInitialProfileActivity()
                finish()
            }
        }

        if (UserApiClient.instance.isKakaoTalkLoginAvailable(this)) {
            UserApiClient.instance.loginWithKakaoTalk(this) { token, error ->
                if (error != null) {

                    // 로그인 취소
                    if (error is ClientError && error.reason == ClientErrorCause.Cancelled) {
                        return@loginWithKakaoTalk
                    }

                    UserApiClient.instance.loginWithKakaoAccount(this, callback = callback)
                } else if (token != null) {
                    startInitialProfileActivity()
                    finish()
                }
            }
        } else {
            UserApiClient.instance.loginWithKakaoAccount(this, callback = callback)
        }
    }

    private fun startInitialProfileActivity() {
        val intent = Intent(this, InitialProfileActivity::class.java)
        startActivity(intent)
    }
}