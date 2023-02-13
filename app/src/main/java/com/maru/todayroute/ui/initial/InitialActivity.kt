package com.maru.todayroute.ui.initial

import android.annotation.SuppressLint
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.widget.Toast
import androidx.activity.viewModels
import com.google.firebase.dynamiclinks.ktx.dynamicLinks
import com.google.firebase.ktx.Firebase
import com.maru.todayroute.R
import com.maru.todayroute.databinding.ActivityInitialBinding
import com.maru.todayroute.ui.MainActivity
import com.maru.todayroute.util.BaseActivity
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class InitialActivity : BaseActivity<ActivityInitialBinding>(R.layout.activity_initial) {

    private val viewModel: InitialViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        viewModel.moveToMainActivity.observe(this) {
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
            finish()
        }

        setInviteCodeAndStartDateFromAppLink()
    }

    @SuppressLint("MissingSuperCall")
    override fun onNewIntent(intent: Intent?) {
        super.onNewIntent(intent)
        setInviteCodeAndStartDateFromAppLink()
    }

    private fun setInviteCodeAndStartDateFromAppLink() {
        Firebase.dynamicLinks
            .getDynamicLink(intent)
            .addOnSuccessListener { pendingDynamicLinkData ->

                var deepLink: Uri? = null
                if (pendingDynamicLinkData != null) {
                    deepLink = pendingDynamicLinkData.link
                }
                if (deepLink != null && deepLink.getBooleanQueryParameter(CODE, false)) {
                    val inviteCode = deepLink.getQueryParameter(CODE)

                    inviteCode?.let { it ->
                        viewModel.setInviteCode(it)
                    }
                }
            }
    }

    companion object {
        const val CODE = "code"
    }
}