package com.maru.todayroute.ui.initial

import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle
import android.provider.MediaStore
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.maru.todayroute.R
import com.maru.todayroute.ui.MainActivity
import com.maru.todayroute.databinding.ActivityInitialProfileBinding
import com.maru.todayroute.util.BaseActivity
import dagger.hilt.android.AndroidEntryPoint
import java.lang.Exception

@AndroidEntryPoint
class InitialProfileActivity :
    BaseActivity<ActivityInitialProfileBinding>(R.layout.activity_initial_profile) {

    private val viewModel by viewModels<InitialProfileViewModel>()

    private val requestGalleryLauncher = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    )
    {
        if (it.resultCode == RESULT_OK && it.data != null) {
            try {
                val currentImageUri = it.data!!.data
                currentImageUri?.let { imageUri ->
                    viewModel.bindingImage(imageUri.toString())
                }
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        binding.viewModel = viewModel

        binding.btnGalleryOpen.setOnClickListener {
            // 버튼을 누른경우 권한 물어봄
            val cameraPermissionCheck = ContextCompat.checkSelfPermission(
                this@InitialProfileActivity,
                android.Manifest.permission.CAMERA
            )

            if (cameraPermissionCheck != PackageManager.PERMISSION_GRANTED) { // 권한이 없다면
                ActivityCompat.requestPermissions(
                    this,
                    arrayOf(android.Manifest.permission.CAMERA),
                    1000
                )
            } else {
                getProfileImageFromGallery()
            }
        }

        binding.btnComplete.setOnClickListener {
            // TODO: 서버에 새로운 User POST 요청
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
            finish()
        }
    }

    private fun getProfileImageFromGallery() {
        val intent = Intent(Intent.ACTION_PICK)
        intent.data = MediaStore.Images.Media.EXTERNAL_CONTENT_URI
        intent.type = "image/*"
        requestGalleryLauncher.launch(intent)
    }

    // 권한 물어본것에 대한 대답 받음
    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == 1000) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) { // 권한 허용
                getProfileImageFromGallery()
            } else {
                // TODO: 권한 거부했을 때
            }
        }
    }
}
