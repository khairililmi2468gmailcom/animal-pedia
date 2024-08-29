package com.example.animalpedia

import android.graphics.Bitmap
import java.nio.ByteBuffer
import java.nio.ByteOrder

object Utils {
    fun convertBitmapToByteBuffer(bitmap: Bitmap): ByteBuffer {
        val size = 240
        val byteBuffer = ByteBuffer.allocateDirect(1 * size * size * 3 * 4)
        byteBuffer.order(ByteOrder.nativeOrder())

        val resizedBitmap = Bitmap.createScaledBitmap(bitmap, size, size, true)
        val intValues = IntArray(size * size)
        resizedBitmap.getPixels(intValues, 0, size, 0, 0, size, size)

        var pixel = 0
        for (i in 0 until size) {
            for (j in 0 until size) {
                val value = intValues[pixel++]
                byteBuffer.putFloat(((value shr 16 and 0xFF) / 255.0f))
                byteBuffer.putFloat(((value shr 8 and 0xFF) / 255.0f))
                byteBuffer.putFloat(((value and 0xFF) / 255.0f))
            }
        }

        return byteBuffer
    }
}
