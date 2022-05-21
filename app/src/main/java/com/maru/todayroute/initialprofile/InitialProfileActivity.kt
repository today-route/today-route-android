package com.maru.todayroute.initialprofile

import android.content.Intent
import android.content.pm.PackageManager
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.maru.todayroute.MainActivity
import com.maru.todayroute.databinding.ActivityInitialProfileBinding
import java.lang.Exception


class InitialProfileActivity : AppCompatActivity() {

    //ActivityInitialProfileBinding은 xml파일을 class로 만들어놓은 것
    private val binding: ActivityInitialProfileBinding by lazy {
        ActivityInitialProfileBinding.inflate(
            layoutInflater
        )
    }

    private val requestGalleryLauncher = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    )
    {
        if (it.resultCode == RESULT_OK && it.data != null) {
            try {
                val currentImageUri = it.data!!.data
                currentImageUri?.let {
                    this.let {
                        // 이전 코드가 사진이 깨져서 수정
                        binding.ivProfileImage.setImageURI(currentImageUri)
                        binding.ivProfileImage.clipToOutline = true
                    }
                }
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

        override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        binding.btnGalleryOpen.setOnClickListener {
            // 버튼을 누른경우 권한 물어봄
            val cameraPermissionCheck = ContextCompat.checkSelfPermission(
                this@InitialProfileActivity,
                android.Manifest.permission.CAMERA      // 원하는 권한
            )

            if (cameraPermissionCheck != PackageManager.PERMISSION_GRANTED) {
                // 권한이 없다면
                ActivityCompat.requestPermissions(  // requestPermission()함수 이용
                    // 받는 인자 : (Activity, 배열, 요청코드) -> 배열 = 권한을 여러개 리스트로 요청할수있음
                    this, // 액티비티 넣어줌
                    arrayOf(android.Manifest.permission.CAMERA), // 카메라 권한 하나만 물어봐도 배열형태로 넣어줘야함
                    1000    // 1000이라는 리퀘스트 코드로 요청함 -> 응답을 받아볼 수 있음
                )
                //Log.d("permissionsss","권한이 없음")
            } else {
                // 권한이 있다면
                Log.d("permissionsss", "권한이 이미 있음")
                getProfileImageFromGallery()
            }
        }

        binding.btnComplete.setOnClickListener {
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
            // request 결과 => grantResults에 있음
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {       // 만약 허가 된상태
                // 승낙
                Log.d("permissionsss", "승낙")
                getProfileImageFromGallery()
            } else {
                // 거부
                Log.d("permissionsss", "거부")
            }
        }
    }
}
