package com.android.cameracapturecomponent.util

import android.content.Context
import android.content.pm.PackageManager
import androidx.core.content.ContextCompat
import com.android.cameracapturecomponent.views.CameraCaptureFragment

object PermissionUtil {
    fun allPermissionsGranted(activity: Context?) = CameraCaptureFragment.REQUIRED_PERMISSIONS.all {
        activity?.let { it1 ->
            ContextCompat.checkSelfPermission(
                it1, it
            )
        } == PackageManager.PERMISSION_GRANTED
    }
}