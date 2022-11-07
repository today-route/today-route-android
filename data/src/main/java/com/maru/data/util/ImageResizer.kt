package com.maru.data.util

import android.graphics.Bitmap
import kotlin.math.round
import kotlin.math.sqrt

class ImageResizer {

    fun resizeBitmap(bitmap: Bitmap, maxSize: Int): Bitmap {
        val bitmapWidth = bitmap.width
        val bitmapHeight = bitmap.height
        val ratioSquare = (bitmapWidth * bitmapHeight).toDouble() / maxSize

        if (ratioSquare <= 1) return bitmap
        val ratio = sqrt(ratioSquare)
        val requireWidth = round(bitmapWidth / ratio).toInt()
        val requireHeight = round(bitmapHeight / ratio).toInt()

        return Bitmap.createScaledBitmap(bitmap, requireWidth, requireHeight, true)
    }
}