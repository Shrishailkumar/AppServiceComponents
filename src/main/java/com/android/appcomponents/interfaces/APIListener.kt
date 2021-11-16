package com.android.appcomponents.interfaces

import okhttp3.ResponseBody

interface APIListener {
    fun onStarted()
    fun onSuccessResponse(responseBody: ResponseBody)
    fun onErrorResponse(errorMessage: String)
}