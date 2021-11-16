package com.android.cameracapturecomponent.interfaces

import android.net.Uri

interface OnImageCaptureCallback {

    fun onError(message: String)

    fun onImageCaptured(capturedImageUri: Uri)
}