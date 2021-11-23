package com.android.cameracapturecomponent.views

import android.Manifest
import android.app.Activity
import android.content.ActivityNotFoundException
import android.content.Intent
import android.graphics.Bitmap
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.Fragment
import com.android.appcomponents.R
import com.android.cameracapturecomponent.interfaces.OnImageCaptureCallback
import com.android.cameracapturecomponent.interfaces.OnVideoCaptureCallback
import com.android.cameracapturecomponent.util.CameraMode
import com.android.cameracapturecomponent.util.FileUtil
import com.android.cameracapturecomponent.util.PermissionUtil

internal class CameraIntentFragment : Fragment() {

    private var onImageCaptureCallback: OnImageCaptureCallback? = null
    private var onVideoCaptureCallback: OnVideoCaptureCallback? = null
    private var shouldShowPreview = false
    private var videoTimeLimit: Long = 10000


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Request camera permissions
        if (PermissionUtil.allPermissionsGranted(activity)) {
            startIntent(cameraMode)
        } else {
            val activityResultLauncher = registerForActivityResult(
                ActivityResultContracts.RequestMultiplePermissions()
            ) { result ->
                var allAreGranted = true
                for (b in result.values) {
                    allAreGranted = allAreGranted && b
                }

                if (allAreGranted) {
                    startIntent(cameraMode)
                } else {
                    Toast.makeText(
                        activity,
                        "Permissions not granted by the user.",
                        Toast.LENGTH_SHORT
                    ).show()
                    activity?.supportFragmentManager?.popBackStack()
                }
            }
            activityResultLauncher.launch(REQUIRED_PERMISSIONS)
        }
        return inflater.inflate(R.layout.fragment_camera_intent, container, false)
    }

    fun setOnImageCaptureCallback(onImageCaptureCallback: OnImageCaptureCallback?) {
        this.onImageCaptureCallback = onImageCaptureCallback
    }

    fun shouldShowPreview(shouldShowPreview: Boolean) {
        this.shouldShowPreview = shouldShowPreview
    }

    fun setonVideoCaptureCallback(onVideoCaptureCallback: OnVideoCaptureCallback?) {
        this.onVideoCaptureCallback = onVideoCaptureCallback
    }

    private fun startIntent(cameraMode: CameraMode) {
        if (cameraMode == CameraMode.PHOTO) {
            val cameraIntent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
            try {
                startActivityForResult(cameraIntent, REQUEST_IMAGE_CAPTURE)
            } catch (e: ActivityNotFoundException) {
                Log.e("Image Captured Error:", e.message ?: "Image Capture Error")
            }
        } else {
            val cameraIntent = Intent(MediaStore.ACTION_VIDEO_CAPTURE)
            cameraIntent.putExtra(MediaStore.EXTRA_DURATION_LIMIT, videoTimeLimit * 1000)
            try {
                startActivityForResult(cameraIntent, REQUEST_VIDEO_CAPTURE)
            } catch (e: ActivityNotFoundException) {
                Log.e("Image Captured Error:", e.message ?: "Image Capture Error")
            }
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (requestCode == REQUEST_IMAGE_CAPTURE || requestCode == REQUEST_VIDEO_CAPTURE && resultCode == Activity.RESULT_OK) {
            if (cameraMode == CameraMode.PHOTO) {
                Intent(MediaStore.ACTION_IMAGE_CAPTURE)
                val imageUri = FileUtil.getImageUri(activity, data?.extras?.get("data") as Bitmap)
                activity?.supportFragmentManager?.popBackStack()
                if (imageUri == null) {
                    onImageCaptureCallback?.onError("Error while capturing image...")
                } else {
                    onImageCaptureCallback?.onImageCaptured(imageUri)
                }
            } else {
                Intent(MediaStore.ACTION_VIDEO_CAPTURE)
                activity?.supportFragmentManager?.popBackStack()
                val videoUri = data?.data
                if (videoUri == null) {
                    onVideoCaptureCallback?.onError("Error while capturing video...")
                } else {
                    onVideoCaptureCallback?.onVideoCaptured(videoUri)
                }
            }
        }
    }

    fun setVideoTimeLimit(limit: Long) {
        videoTimeLimit = limit
    }

    companion object {
        private var cameraMode: CameraMode = CameraMode.PHOTO
        fun getInstance(
            cameraMode: CameraMode
        ): CameraIntentFragment {
            this.cameraMode = cameraMode
            return CameraIntentFragment()
        }

        private const val REQUEST_IMAGE_CAPTURE = 90
        private const val REQUEST_VIDEO_CAPTURE = 91
        val REQUIRED_PERMISSIONS = arrayOf(
            Manifest.permission.CAMERA,
            Manifest.permission.RECORD_AUDIO,
            Manifest.permission.WRITE_EXTERNAL_STORAGE
        )
    }
}