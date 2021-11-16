package com.android.cameracapturecomponent.interfaces

import android.net.Uri

interface OnVideoCaptureCallback {

    fun onError(message: String)

    fun onVideoCaptured(capturedVideoUri: Uri)
}