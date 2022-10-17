package com.maru.todayroute.ui.splash

import android.os.Bundle
import androidx.activity.viewModels
import androidx.lifecycle.lifecycleScope
import com.maru.todayroute.R
import com.maru.todayroute.databinding.ActivitySplashBinding
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

        }

        viewModel.moveToInitialActivity.observe(this) { user ->
            if (user == null) {

            } else {

            }
            finish()
        }
    }
}