package com.maru.todayroute.util

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Matrix
import android.media.ExifInterface
import android.net.Uri
import android.os.Environment
import android.provider.MediaStore
import android.util.Log
import java.io.ByteArrayOutputStream
import java.io.File
import java.io.FileOutputStream
import kotlin.math.round
import kotlin.math.sqrt

object ImageHandler {

    fun optimizeImage(bitmap: Bitmap, quality: Int): File? {
        val directory = File(
            Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DCIM),
            "TodayRoute"
        )
        if (!directory.exists()) {
            directory.mkdir()
        }
        val file = File(
            Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DCIM),
            "TodayRoute/" + System.currentTimeMillis() + ".jpg"
        )
        val fileOutputStream = FileOutputStream(file)
        val byteArrayOutputStream = ByteArrayOutputStream()
        bitmap.apply {
            compress(Bitmap.CompressFormat.JPEG, quality, byteArrayOutputStream)
        }
        val bitmapData = byteArrayOutputStream.toByteArray()

        try {
            file.createNewFile()
            fileOutputStream.write(bitmapData)
            fileOutputStream.flush()
            fileOutputStream.close()

            return file
        } catch (e: Exception) {
            Log.e("Error", "${e.message}")
        }

        return null
    }

    fun decodeBitmapFromUri(path: String): Bitmap? {
        return BitmapFactory.Options().run {
            inJustDecodeBounds = true
            inJustDecodeBounds = false

            val bitmap = BitmapFactory.decodeFile(path, this)
            val resizedBitmap = resizeBitmap(bitmap, 360000)
            rotateImageIfRequired(resizedBitmap, path)
        }
    }

    private fun resizeBitmap(bitmap: Bitmap, maxSize: Int): Bitmap {
        val bitmapWidth = bitmap.width
        val bitmapHeight = bitmap.height
        val ratioSquare = (bitmapWidth * bitmapHeight).toDouble() / maxSize

        if (ratioSquare <= 1) return bitmap
        val ratio = sqrt(ratioSquare)
        val requireWidth = round(bitmapWidth / ratio).toInt()
        val requireHeight = round(bitmapHeight / ratio).toInt()

        return Bitmap.createScaledBitmap(bitmap, requireWidth, requireHeight, true)
    }

    private fun rotateImageIfRequired(bitmap: Bitmap, path: String): Bitmap? {
        val exif = ExifInterface(path)

        return when (exif.getAttributeInt(
            ExifInterface.TAG_ORIENTATION,
            ExifInterface.ORIENTATION_NORMAL
        )) {
            ExifInterface.ORIENTATION_ROTATE_90 -> rotateImage(bitmap, 90)
            ExifInterface.ORIENTATION_ROTATE_180 -> rotateImage(bitmap, 180)
            ExifInterface.ORIENTATION_ROTATE_270 -> rotateImage(bitmap, 270)
            else -> bitmap
        }
    }

    private fun rotateImage(bitmap: Bitmap, degree: Int): Bitmap? {
        val matrix = Matrix()
        matrix.postRotate(degree.toFloat())
        return Bitmap.createBitmap(bitmap, 0, 0, bitmap.width, bitmap.height, matrix, true)
    }

    @SuppressLint("Range")
    fun getRealPathFromUriList(context: Context, uri: Uri): String {
        val proj = arrayOf(MediaStore.Images.Media.DATA)

        val cursor = context.contentResolver.query(uri, proj, null, null, null)
        cursor!!.moveToNext()
        val path = cursor.getString(cursor.getColumnIndex(MediaStore.MediaColumns.DATA))
        cursor.close()

        return path
    }
}