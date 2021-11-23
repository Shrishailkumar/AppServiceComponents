package com.android.cameracapturecomponent

import androidx.fragment.app.Fragment
import com.android.cameracapturecomponent.interfaces.OnImageCaptureCallback
import com.android.cameracapturecomponent.interfaces.OnVideoCaptureCallback
import com.android.cameracapturecomponent.util.CameraMode
import com.android.cameracapturecomponent.util.CameraSource
import com.android.cameracapturecomponent.views.CameraCaptureFragment
import com.android.cameracapturecomponent.views.CameraIntentFragment

class CameraComponent {

    private var cameraCaptureFragment: CameraCaptureFragment? = null
    private var cameraIntentFragment: CameraIntentFragment? = null
    private var cameraSource: CameraSource = CameraSource.CUSTOM

    /**
     * - This component is developed to use android camera feature to capture the photo, video or both.
     * - This supports both primary (main camera sensor on back) and secondary camera (selfie camera).
     * - This allows user to take a photo or video from camera and returns the uri (path of the file from storage) so It can be further used in app as needed.
     *
     * @param cameraMode This param used to define the mode of camera, CameraMode.PHOTO, CameraMode.VIDEO or CameraMode.BOTH
     * @param shouldShowPreview This param is used for setting weather to show preview after capturing video or photo
     * @param imageCaptureCallback This param is used to set the OnImageCaptureCallback callback
     * @param videoCaptureCallback This param is used to set the OnVideoCaptureCallback callback
     * @param videoLimit This param is used to set the time limit for video
     * @param captureFromIntent This param is used to set the source of camera which can be either Intent or Custom
     *
     * @return Fragment? This method will returns the fragment object with camera initialized
     */
    fun initCameraPreview(
        cameraMode: CameraMode = CameraMode.PHOTO,
        shouldShowPreview: Boolean = false,
        imageCaptureCallback: OnImageCaptureCallback? = null,
        videoCaptureCallback: OnVideoCaptureCallback? = null,
        videoLimit: Long = 10000,
        cameraSource: CameraSource = CameraSource.CUSTOM
    ): Fragment? {
        if (cameraMode == CameraMode.BOTH) {
            this.cameraSource = CameraSource.CUSTOM
        } else {
            this.cameraSource = cameraSource
        }
        if (cameraSource == CameraSource.INTENT) {
            cameraIntentFragment =
                CameraIntentFragment.getInstance(cameraMode)
            cameraIntentFragment?.setOnImageCaptureCallback(imageCaptureCallback)
            cameraIntentFragment?.setonVideoCaptureCallback(videoCaptureCallback)
            cameraIntentFragment?.shouldShowPreview(shouldShowPreview)
            cameraIntentFragment?.setVideoTimeLimit(videoLimit)
            return cameraIntentFragment
        } else {
            cameraCaptureFragment =
                CameraCaptureFragment.getInstance(cameraMode)
            cameraCaptureFragment?.setOnImageCaptureCallback(imageCaptureCallback)
            cameraCaptureFragment?.setonVideoCaptureCallback(videoCaptureCallback)
            cameraCaptureFragment?.shouldShowPreview(shouldShowPreview)
            cameraCaptureFragment?.setVideoTimeLimit(videoLimit)
            return cameraCaptureFragment
        }
    }

    /**
     * This method is used to set the OnVideoCaptureCallback callback explicitly
     *
     * @param onVideoCaptureCallback This param is used to set the OnVideoCaptureCallback callback
     */
    fun setVideoCaptureCallback(onVideoCaptureCallback: OnVideoCaptureCallback) {
        if (cameraSource == CameraSource.INTENT) {
            cameraIntentFragment?.setonVideoCaptureCallback(onVideoCaptureCallback)
        } else {
            cameraCaptureFragment?.setonVideoCaptureCallback(onVideoCaptureCallback)
        }
    }

    /**
     * This method is used to set the OnImageCaptureCallback callback explicitly
     *
     * @param onImageCaptureCallback This param is used to set the OnImageCaptureCallback callback
     */
    fun setImageCaptureCallback(onImageCaptureCallback: OnImageCaptureCallback) {
        if (cameraSource == CameraSource.INTENT) {
            cameraIntentFragment?.setOnImageCaptureCallback(onImageCaptureCallback)
        } else {
            cameraCaptureFragment?.setOnImageCaptureCallback(onImageCaptureCallback)
        }
    }

    /**
     * This method is used to set the shouldShowPreview explicitly
     *
     * @param enablePreview This param is used to set the shouldShowPreview
     */
    fun enabledPreview(enablePreview: Boolean) {
        if (cameraSource == CameraSource.INTENT) {
            cameraIntentFragment?.shouldShowPreview(enablePreview)
        } else {
            cameraCaptureFragment?.shouldShowPreview(enablePreview)
        }
    }

    /**
     * This method is used to set the videoLimit explicitly
     *
     * @param videoLimit This param is used to set the videoLimit
     */
    fun setVideoTimeLimit(videoLimit: Long) {
        if (cameraSource == CameraSource.INTENT) {
            cameraIntentFragment?.setVideoTimeLimit(videoLimit)
        } else {
            cameraCaptureFragment?.setVideoTimeLimit(videoLimit)
        }
    }
}