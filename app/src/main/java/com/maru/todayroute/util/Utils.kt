package com.maru.todayroute.util

import android.Manifest
import android.app.Activity
import android.app.DatePickerDialog
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.provider.MediaStore
import android.view.inputmethod.InputMethodManager
import android.widget.EditText
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import com.maru.todayroute.R
import com.naver.maps.geometry.LatLng
import java.text.SimpleDateFormat
import java.util.*

object Utils {

    fun String.convertSingleToDoubleDigit(): String = if (this.length < 2) "0$this" else this

    fun showDatePicker(context: Context, editText: EditText, d: Triple<Int, Int, Int>, setDate: (Int, Int, Int) -> Unit) {
        DatePickerDialog(context,
            { _, year, month, date ->
                setDate(year, month, date)
                editText.setText(
                    context.getString(R.string.format_date,
                            year,
                            (month + 1).toString().convertSingleToDoubleDigit(),
                            date.toString().convertSingleToDoubleDigit()
                    )
                )
            },
            d.first,
            d.second,
            d.third
        ).show()
    }

    fun calculateDDay(startDate: String): String {
        val currentTimeMillis = System.currentTimeMillis()
        val simpleDataFormat = SimpleDateFormat("yyyy-MM-dd", Locale.KOREAN)
        val startDateInMillis = simpleDataFormat.parse(startDate)?.time!!

        return "${(currentTimeMillis - startDateInMillis) / (24 * 60 * 60 * 1000) + 1}"
    }

    fun getCurrentDate(): String {
        val currentTimeMillis = System.currentTimeMillis()
        val simpleDataFormat = SimpleDateFormat("yyyy-MM-dd", Locale.KOREAN)
        return simpleDataFormat.format(currentTimeMillis)
    }
}

object RouteUtils {
    fun calculateCenterCoordinate(latitudeList: List<Double>, longitudeList: List<Double>): LatLng {
        val minLatitude = latitudeList.minOrNull() ?: 0.0
        val maxLatitude = latitudeList.maxOrNull() ?: 0.0
        val minLongitude = longitudeList.minOrNull() ?: 0.0
        val maxLongitude = longitudeList.maxOrNull() ?: 0.0
        val centerLatitude = (minLatitude + maxLatitude) / 2
        val centerLongitude = (minLongitude + maxLongitude) / 2

        return LatLng(centerLatitude, centerLongitude)
    }
}

object AccessGalleryUtils {
    fun selectMultipleImagesFromGalleryLauncher(
        fragment: Fragment,
        addPhotos: (List<Uri>) -> Unit
    ) =
        fragment.registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            if (result.resultCode == Activity.RESULT_OK) {
                val photoList = mutableListOf<Uri>()

                if (result.data?.clipData != null) {
                    val data = result.data?.clipData
                    data?.let { clipData ->
                        for (i in 0 until clipData.itemCount) {
                            val uri = clipData.getItemAt(i).uri
                            photoList.add(uri)
                        }
                    }
                } else {
                    // 이미지를 한 장만 선택한 경우
                    result.data?.data?.let { uri ->
                        photoList.add(uri)
                    }
                }
                addPhotos.invoke(photoList)
            }
        }

    fun selectMultipleImagesFromGallery(launcher: ActivityResultLauncher<Intent>) {
        val intent = Intent(Intent.ACTION_PICK)
        intent.setDataAndType(
            MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
            MediaStore.Images.Media.CONTENT_TYPE
        )
        intent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true)
        launcher.launch(intent)
    }
}

object RequestPermissionsUtils {

    fun hasPermission(context: Context, permissionList: List<String>): Boolean {
        permissionList.forEach { permission ->
            if (ContextCompat.checkSelfPermission(
                    context,
                    permission
                ) != PackageManager.PERMISSION_GRANTED
            ) return false
        }
        return true
    }

    fun externalStoragePermissionRequest(fragment: Fragment, openGallery: () -> Unit) =
        fragment.registerForActivityResult(
            ActivityResultContracts.RequestMultiplePermissions()
        ) { permissions ->
            when {
                permissions[Manifest.permission.READ_EXTERNAL_STORAGE] ?: false
                        || permissions[Manifest.permission.WRITE_EXTERNAL_STORAGE] ?: false
                -> {
                    openGallery.invoke()
                }
            }
        }
}

fun hideKeyboard(activity: Activity) {
    val inputMethodManager = activity.getSystemService(Activity.INPUT_METHOD_SERVICE) as InputMethodManager
    inputMethodManager.hideSoftInputFromWindow(activity.currentFocus?.windowToken, InputMethodManager.HIDE_NOT_ALWAYS)
}