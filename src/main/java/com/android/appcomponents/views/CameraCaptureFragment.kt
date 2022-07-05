package com.android.cameracapturecomponent.views

import android.Manifest
import android.annotation.SuppressLint
import android.net.Uri
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.provider.MediaStore
import android.util.Log
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.camera.core.*
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import com.android.cameracapturecomponent.interfaces.OnImageCaptureCallback
import com.android.cameracapturecomponent.interfaces.OnVideoCaptureCallback
import com.android.cameracapturecomponent.util.*
import kotlinx.android.synthetic.main.fragment_camera.*
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import java.util.concurrent.ExecutorService
import java.util.concurrent.Executors
import com.android.appcomponents.R

internal class CameraCaptureFragment : Fragment() {

    private var onImageCaptureCallback: OnImageCaptureCallback? = null
    private var onVideoCaptureCallback: OnVideoCaptureCallback? = null
    private var shouldShowPreview = false
    private var videoTimeLimit: Long = 10000

    private var imageCapture: ImageCapture? = null
    private var videoCapture: VideoCapture? = null

    private lateinit var cameraExecutor: ExecutorService

    private var lensFacing = CameraSelector.DEFAULT_BACK_CAMERA
    private var isVideoRecording = false
    private val videoStop = Handler(Looper.getMainLooper())

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Request camera permissions
        if (PermissionUtil.allPermissionsGranted(activity)) {
            startCamera(cameraMode)
        } else {
            val activityResultLauncher = registerForActivityResult(
                ActivityResultContracts.RequestMultiplePermissions()
            ) { result ->
                var allAreGranted = true
                for (b in result.values) {
                    allAreGranted = allAreGranted && b
                }

                if (allAreGranted) {
                    startCamera(cameraMode)
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
        return inflater.inflate(R.layout.fragment_camera, container, false)
    }

    @SuppressLint("ClickableViewAccessibility", "RestrictedApi")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val handler = Handler(Looper.getMainLooper())
        if (cameraMode == CameraMode.VIDEO) {
            btnCameraCapture.setOnClickListener {
                if (isVideoRecording) {
                    stopVideoRecording()
                } else {
                    recordVideo()
                }
            }
        } else {
            btnCameraCapture.setOnTouchListener { _, event ->

                when (event.action) {
                    MotionEvent.ACTION_DOWN -> {
                        if (cameraMode != CameraMode.PHOTO) {
                            handler.postDelayed(imageVideoCheckRunnable, 300)
                        }
                    }

                    MotionEvent.ACTION_UP -> {
                        handler.removeCallbacks(imageVideoCheckRunnable)
                        if (isVideoRecording) {
                            stopVideoRecording()
                        } else {
                            takePhoto()
                        }
                    }
                }
                true
            }
        }

        if (cameraMode == CameraMode.BOTH) {
            tvVideoInfo.visible()
        }

        btnSwapCamera.setOnClickListener { swapCamera() }

        btnFlashToggle.setOnClickListener { toggleFlash() }

        cameraExecutor = Executors.newSingleThreadExecutor()
    }

    @SuppressLint("RestrictedApi")
    private fun stopVideoRecording() {
        videoStop.removeCallbacks(videoStopRunnable)
        videoCapture?.stopRecording()
        btnCameraCaptureInner?.stopAnimation()
    }

    @SuppressLint("RestrictedApi")
    private val imageVideoCheckRunnable = Runnable {
        recordVideo()
    }

    @SuppressLint("RestrictedApi")
    private val videoStopRunnable = Runnable {
        videoCapture?.stopRecording()
        btnCameraCaptureInner?.stopAnimation()
    }

    private fun toggleFlash() {
        val imageCapture = imageCapture ?: return
        when (imageCapture.flashMode) {
            ImageCapture.FLASH_MODE_ON -> {
                imageCapture.flashMode = ImageCapture.FLASH_MODE_AUTO
                btnFlashToggle.setImageResource(R.drawable.ic_baseline_flash_auto_24)
            }
            ImageCapture.FLASH_MODE_AUTO -> {
                imageCapture.flashMode = ImageCapture.FLASH_MODE_OFF
                btnFlashToggle.setImageResource(R.drawable.ic_baseline_flash_off_24)
            }
            else -> {
                imageCapture.flashMode = ImageCapture.FLASH_MODE_ON
                btnFlashToggle.setImageResource(R.drawable.ic_baseline_flash_on_24)
            }
        }
    }

    private fun swapCamera() {
        if (lensFacing == CameraSelector.DEFAULT_FRONT_CAMERA) lensFacing =
            CameraSelector.DEFAULT_BACK_CAMERA
        else if (lensFacing == CameraSelector.DEFAULT_BACK_CAMERA) lensFacing =
            CameraSelector.DEFAULT_FRONT_CAMERA

        btnSwapCamera.animate().withLayer()
            .rotationY(90F)
            .setDuration(300)
            .withEndAction {
                btnSwapCamera.rotationY = -90F
                btnSwapCamera.animate().withLayer()
                    .rotationY(0F)
                    .setDuration(300)
                    .start()
            }.start()

        startCamera(cameraMode)
    }

    private fun takePhoto() {
        // Get a stable reference of the modifiable image capture use case
        val imageCapture = imageCapture ?: return

        val imageFile = FileUtil.getImageFileName(activity)
        // Create output options object which contains file + metadata
        val outputOptions =
            ImageCapture.OutputFileOptions.Builder(imageFile)
                .build()

        context?.let { ContextCompat.getMainExecutor(it) }?.let {

            GlobalScope.launch {
                btnCameraCaptureInner.scaleInAnimation(200)
                delay(200)
                btnCameraCaptureInner.scaleOutAnimation(200)
            }

            imageCapture.takePicture(
                outputOptions,
                it,
                object : ImageCapture.OnImageSavedCallback {
                    override fun onError(exc: ImageCaptureException) {
                        Log.e(TAG, "Photo capture failed: ${exc.message}", exc)
                        activity?.supportFragmentManager?.popBackStack()
                    }

                    override fun onImageSaved(output: ImageCapture.OutputFileResults) {
                        val savedUri = Uri.fromFile(imageFile)
                        if (shouldShowPreview) {
                            activity?.supportFragmentManager?.beginTransaction()?.add(
                                fContainer.id,
                                CameraCapturePreviewFragment.newInstance(
                                    savedUri,
                                    MediaStore.Files.FileColumns.MEDIA_TYPE_IMAGE,
                                    onImageCaptureCallback,
                                    onVideoCaptureCallback
                                )
                            )?.addToBackStack(CameraCapturePreviewFragment::class.java.name)?.commit()
                        } else {
                            onImageCaptureCallback?.onImageCaptured(savedUri)
                            //activity?.supportFragmentManager?.popBackStack()
                        }
                    }
                })
        }
    }

    @SuppressLint("RestrictedApi")
    private fun recordVideo() {

        // Get a stable reference of the modifiable image capture use case
        val videoCapture = videoCapture ?: return

        val videoFile = FileUtil.getVideoFileName(activity)
        // Create output options object which contains file + metadata
        val outputOptions =
            VideoCapture.OutputFileOptions.Builder(videoFile).build()

        context?.let { ContextCompat.getMainExecutor(it) }?.let {

            btnCameraCaptureInner.startColorBlinkAnimation()
            videoCapture.startRecording(
                outputOptions,
                it,
                object : VideoCapture.OnVideoSavedCallback {
                    override fun onVideoSaved(outputFileResults: VideoCapture.OutputFileResults) {
                        Log.e("Video Success", outputFileResults.savedUri.toString())
                        isVideoRecording = false
                        val savedUri = Uri.fromFile(videoFile)
                        if (shouldShowPreview) {
                            activity?.supportFragmentManager?.beginTransaction()?.add(
                                fContainer.id,
                                CameraCapturePreviewFragment.newInstance(
                                    savedUri,
                                    MediaStore.Files.FileColumns.MEDIA_TYPE_VIDEO,
                                    onImageCaptureCallback,
                                    onVideoCaptureCallback
                                )
                            )?.addToBackStack(CameraCapturePreviewFragment::class.java.name)?.commit()
                        } else {
                            onVideoCaptureCallback?.onVideoCaptured(savedUri)
                            activity?.supportFragmentManager?.popBackStack()
                        }
                    }

                    override fun onError(
                        videoCaptureError: Int,
                        message: String,
                        cause: Throwable?
                    ) {
                        Log.e("Video Error", message)
                        isVideoRecording = false
                        activity?.supportFragmentManager?.popBackStack()
                    }
                })
        }
        if (videoTimeLimit > -1) {
            videoStop.postDelayed(videoStopRunnable, videoTimeLimit)
        }
        isVideoRecording = true
    }

    private fun startCamera(cameraMode: CameraMode) {
        val cameraProviderFuture = activity?.let { ProcessCameraProvider.getInstance(it) }

        activity?.let { it ->
            cameraProviderFuture?.addListener({
                val cameraProvider: ProcessCameraProvider = cameraProviderFuture.get()

                val preview = Preview.Builder()
                    .build()
                    .also {
                        it.setSurfaceProvider(viewFinder.surfaceProvider)
                    }

                val cameraSelector = lensFacing

                when (cameraMode) {
                    CameraMode.PHOTO -> {
                        imageCapture = ImageCapture.Builder().build()
                        try {
                            cameraProvider.unbindAll()
                            cameraProvider.bindToLifecycle(
                                this,
                                cameraSelector,
                                preview,
                                imageCapture
                            )
                        } catch (exc: Exception) {
                            Log.e(TAG, "Use case binding failed", exc)
                        }
                    }
                    CameraMode.VIDEO -> {
                        videoCapture = VideoCapture.Builder().build()
                        try {
                            cameraProvider.unbindAll()
                            cameraProvider.bindToLifecycle(
                                this,
                                cameraSelector,
                                preview,
                                videoCapture
                            )
                        } catch (exc: Exception) {
                            Log.e(TAG, "Use case binding failed", exc)
                        }
                    }
                    CameraMode.BOTH -> {
                        imageCapture = ImageCapture.Builder().build()
                        videoCapture = VideoCapture.Builder().build()
                        try {
                            cameraProvider.unbindAll()
                            cameraProvider.bindToLifecycle(
                                this,
                                cameraSelector,
                                preview,
                                imageCapture,
                                videoCapture
                            )
                        } catch (exc: Exception) {
                            Log.e(TAG, "Use case binding failed", exc)
                        }
                    }
                }

            }, ContextCompat.getMainExecutor(it))
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        cameraExecutor.shutdown()
        imageCapture = null
        videoCapture = null
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

    fun setVideoTimeLimit(limit: Long) {
        videoTimeLimit = limit
    }

    companion object {
        private var cameraMode: CameraMode = CameraMode.PHOTO
        fun getInstance(
            cameraMode: CameraMode
        ): CameraCaptureFragment {
            this.cameraMode = cameraMode
            return CameraCaptureFragment()
        }

        private const val TAG = "CameraCaptureComponent"
        val REQUIRED_PERMISSIONS = arrayOf(
            Manifest.permission.CAMERA,
            Manifest.permission.RECORD_AUDIO,
            Manifest.permission.WRITE_EXTERNAL_STORAGE
        )
    }
}