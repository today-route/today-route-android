package com.maru.todayroute.initialprofile

import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.BitmapFactory
import android.net.Uri
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

    private val binding: ActivityInitialProfileBinding by lazy {
        ActivityInitialProfileBinding.inflate(
            layoutInflater
        )
    }
    //ActivityInitialProfileBinding 는 xml파일을 class로 만들어놓은것

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        val requestGalleryLauncher = registerForActivityResult(
            ActivityResultContracts.StartActivityForResult()
        )
        {
            try {
                val calculateRatio = calculateInSampleSize(
                    it.data!!.data!!,
                    resources.getDimensionPixelSize(com.google.android.material.R.dimen.design_fab_image_size),
                    resources.getDimensionPixelSize(com.google.android.material.R.dimen.design_fab_image_size)
                )
                val option = BitmapFactory.Options()
                option.inSampleSize = calculateRatio

                var inputStream = contentResolver.openInputStream(it.data!!.data!!)
                val bitmap = BitmapFactory.decodeStream(inputStream, null, option)
                inputStream!!.close()
                inputStream = null

                bitmap?.let {
                    binding.ivProfileImage.clipToOutline = true
                    binding.ivProfileImage.setImageBitmap(bitmap)
                } ?: let {
                    Log.d("bitmapp", "bitmap null")
                }
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }

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
                val intent = Intent(Intent.ACTION_PICK)
                intent.data = MediaStore.Images.Media.EXTERNAL_CONTENT_URI
                intent.type = "image/*"
                requestGalleryLauncher.launch(intent)

            }
        }

        binding.btnComplete.setOnClickListener {
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
            finish()
        }
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

            } else {
                // 거부
                Log.d("permissionsss", "거부")

            }

        }
    }

    // 이미지 비율맞춰 크기 줄이기
    private fun calculateInSampleSize(fileUri: Uri, reqWidth: Int, reqHeighth: Int): Int {
        val options = BitmapFactory.Options()
        options.inJustDecodeBounds = true
        try {
            var inputStream = contentResolver.openInputStream(fileUri)
            BitmapFactory.decodeStream(inputStream, null, options)
            inputStream!!.close()
            inputStream = null
        } catch (e: Exception) {
            e.printStackTrace()
        }

        val (height: Int, width: Int) = options.run { outHeight to outWidth }
        var inSampleSize = 1

        // inSampleSize 비율계산
        if (height > reqHeighth || width > reqWidth) {
            val halfHeight: Int = height / 2
            val halfWidth: Int = width / 2
            while (halfHeight / inSampleSize >= reqHeighth && halfWidth / inSampleSize >= reqWidth) {
                inSampleSize *= 2
            }
        }
        return inSampleSize
    }
}




