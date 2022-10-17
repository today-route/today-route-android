package com.maru.todayroute.ui.splash

import android.content.Intent
import android.os.Bundle
import androidx.activity.viewModels
import androidx.lifecycle.lifecycleScope
import com.maru.todayroute.R
import com.maru.todayroute.databinding.ActivitySplashBinding
import com.maru.todayroute.ui.initial.InitialActivity
import com.maru.todayroute.util.BaseActivity
import kotlinx.coroutines.launch

class SplashActivity : BaseActivity<ActivitySplashBinding>(R.layout.activity_splash) {

    private val viewModel: SplashViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)


        setupObserver()
        checkSignInState()
    }

    private fun checkSignInState() {
        lifecycleScope.launch {
            viewModel.checkSignInState()
        }
    }

    private fun setupObserver() {
        viewModel.moveToMainActivity.observe(this) {
            // dataStore에 access token이 없을 경우
            val intent = Intent(this, InitialActivity::class.java)
            startActivity(intent)
            finish()
        }

        viewModel.moveToInitialActivity.observe(this) { user ->
            if (user == null) {

            } else {

            }
            finish()
        }
    }
}