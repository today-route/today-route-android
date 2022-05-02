package com.maru.todayroute.initialprofile

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.maru.todayroute.R
import com.maru.todayroute.databinding.ActivityInitialProfileBinding

class InitialProfileActivity : AppCompatActivity() {

    private val binding: ActivityInitialProfileBinding by lazy { ActivityInitialProfileBinding.inflate(layoutInflater) }
    //ActivityInitialProfileBinding 는 xml파일을 class로 만들어놓은것

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)



    }
}