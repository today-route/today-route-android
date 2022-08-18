package com.maru.todayroute.ui.initial

import androidx.activity.viewModels
import com.maru.todayroute.R
import com.maru.todayroute.databinding.ActivityInitialBinding
import com.maru.todayroute.util.BaseActivity
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class InitialActivity : BaseActivity<ActivityInitialBinding>(R.layout.activity_initial) {

    private val viewModel: InitialViewModel by viewModels()
}