package com.android.appcomponents.views


import android.os.Build
import android.os.Bundle
import androidx.annotation.RequiresApi
import com.android.appcomponents.R
import androidx.appcompat.app.AppCompatActivity
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.camera.view.PreviewView

import com.google.common.util.concurrent.ListenableFuture
import java.util.concurrent.ExecutionException
import com.android.appcomponents.util.ScannerUtility


class ScannerActivity : AppCompatActivity() {

    private lateinit var cameraProviderFuture: ListenableFuture<ProcessCameraProvider>
    lateinit var previewView: PreviewView

    @RequiresApi(Build.VERSION_CODES.LOLLIPOP)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_scanner2)
        previewView = findViewById(R.id.viewFindPreview)
        cameraProviderFuture = ProcessCameraProvider.getInstance(this)
        this.window.setFlags(1024, 1024)

        ScannerUtility.initialization(previewView, this)

        ScannerUtility.checkPermission(cameraProviderFuture)

    }

    @RequiresApi(Build.VERSION_CODES.LOLLIPOP)
    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == 101 && grantResults.size > 0) {
            var processCameraProvider2: ProcessCameraProvider? = null
            try {
                processCameraProvider2 = cameraProviderFuture.get() as ProcessCameraProvider
            } catch (e: ExecutionException) {
                e.printStackTrace()
            }
            ScannerUtility.bindPreview(processCameraProvider2!!)
        }
    }
}