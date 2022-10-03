package com.maru.todayroute.ui.initial

import android.net.Uri
import android.os.Bundle
import android.widget.Toast
import androidx.activity.viewModels
import com.google.firebase.dynamiclinks.ktx.dynamicLinks
import com.google.firebase.ktx.Firebase
import com.maru.todayroute.R
import com.maru.todayroute.databinding.ActivityInitialBinding
import com.maru.todayroute.util.BaseActivity
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class InitialActivity : BaseActivity<ActivityInitialBinding>(R.layout.activity_initial) {

    private val viewModel: InitialViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        checkInviteCode()
    }

    private fun checkInviteCode() {
        Firebase.dynamicLinks
            .getDynamicLink(intent)
            .addOnSuccessListener { pendingDynamicLinkData ->

                var deepLink: Uri? = null
                if (pendingDynamicLinkData != null) {
                    deepLink = pendingDynamicLinkData.link
                }
                if (deepLink != null && deepLink.getBooleanQueryParameter("code", false)) {
                    val inviteCode = deepLink.getQueryParameter("code")

                    inviteCode?.let { it ->
                        viewModel.setInviteCode(inviteCode)
//                        Toast.makeText(this, it, Toast.LENGTH_SHORT).show()
                    }
                }
            }
    }
}