package com.android.cameracapturecomponent.util

import androidx.fragment.app.FragmentActivity
import java.io.File
import java.text.SimpleDateFormat
import java.util.*
import android.provider.MediaStore
import android.graphics.Bitmap
import android.net.Uri


internal object FileUtil {

    private const val FILENAME_FORMAT = "yyyy-MM-dd-HH-mm-ss-SSS"

    private fun getOutputDirectory(activity: FragmentActivity?): File? {
        val mediaDir = activity?.externalMediaDirs?.firstOrNull()?.let {
            File(it, "CameraComponent").apply { mkdirs() }
        }
        return if (mediaDir != null && mediaDir.exists())
            mediaDir else activity?.filesDir
    }

    fun getImageFileName(activity: FragmentActivity?): File {
        return File(
            getOutputDirectory(activity),
            SimpleDateFormat(
                FILENAME_FORMAT, Locale.US
            ).format(System.currentTimeMillis()) + ".jpg"
        )
    }

    fun getVideoFileName(activity: FragmentActivity?): File {
        return File(
            getOutputDirectory(activity),
            SimpleDateFormat(
                FILENAME_FORMAT, Locale.US
            ).format(System.currentTimeMillis()) + ".mp4"
        )
    }

    fun getImageUri(activity: FragmentActivity?, inImage: Bitmap?): Uri? {
        val outputImage = inImage?.let { Bitmap.createScaledBitmap(it, 1000, 1000, true) }
        val path = MediaStore.Images.Media.insertImage(
            activity?.contentResolver,
            outputImage,
            getImageFileName(activity).name,
            null
        )
        return Uri.parse(path)
    }
}