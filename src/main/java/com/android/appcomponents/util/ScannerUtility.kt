package com.android.appcomponents.util

import android.Manifest
import android.annotation.SuppressLint
import android.app.Activity
import android.app.ActivityManager
import android.content.Context
import android.content.Intent
import android.content.pm.ApplicationInfo
import android.content.pm.PackageInfo
import android.content.pm.PackageManager
import android.graphics.Point
import android.graphics.Rect
import android.os.BatteryManager
import android.os.Build
import android.util.Log
import android.util.Size
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.camera.core.*
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.camera.view.PreviewView
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.lifecycle.MutableLiveData
import com.android.appcomponents.AppContext.AppContext
import com.android.appcomponents.model.DeviceInfo
import com.android.appcomponents.views.ScannerActivity
import com.google.android.gms.tasks.Task
import com.google.common.util.concurrent.ListenableFuture
import com.google.mlkit.vision.barcode.BarcodeScannerOptions
import com.google.mlkit.vision.barcode.BarcodeScanning
import com.google.mlkit.vision.barcode.common.Barcode
import com.google.mlkit.vision.common.InputImage
import java.lang.Exception
import java.sql.Timestamp
import java.text.DecimalFormat
import java.text.SimpleDateFormat
import java.util.*
import java.util.concurrent.ExecutionException
import java.util.concurrent.ExecutorService
import java.util.concurrent.Executors

object ScannerUtility {


    private var cameraExecutor = Executors.newSingleThreadExecutor()

    private lateinit var previewView: PreviewView
    private lateinit var scannerActivity: ScannerActivity

    fun getResult(): MutableLiveData<String> {

        val resultString = MutableLiveData<String>()



        resultString.postValue("")

        return resultString
    }


    fun initialization(previewView: PreviewView, scannerActivity: ScannerActivity) {
        this.previewView = previewView
        this.scannerActivity = scannerActivity
    }


    @RequiresApi(Build.VERSION_CODES.LOLLIPOP)
    fun checkPermission(cameraProviderFuture: ListenableFuture<ProcessCameraProvider>) {
        cameraProviderFuture.addListener({
            try {
                if (ActivityCompat.checkSelfPermission(scannerActivity, Manifest.permission.CAMERA)
                    == PackageManager.PERMISSION_DENIED
                ) {
                    ActivityCompat.requestPermissions(
                        scannerActivity,
                        arrayOf(Manifest.permission.CAMERA), 101
                    )
                } else {
                    val processCameraProvider = cameraProviderFuture.get() as ProcessCameraProvider
                    bindPreview(
                        processCameraProvider
                    )
                }
            } catch (e: ExecutionException) {
                e.printStackTrace()
            }
        }, ContextCompat.getMainExecutor(scannerActivity));

    }


    @RequiresApi(Build.VERSION_CODES.LOLLIPOP)
    fun bindPreview(
        processCameraProvider: ProcessCameraProvider
    ) {
        val preview = Preview.Builder().build()
        val cameraSelector = CameraSelector.Builder().requireLensFacing(
            CameraSelector.LENS_FACING_BACK
        ).build()
        preview.setSurfaceProvider(previewView.surfaceProvider)
        processCameraProvider.unbindAll()
        val imageCapture: ImageCapture = ImageCapture.Builder().build()
        val myImageAnalyzer = MyImageAnalyzer(false)
        val imageAnalysis = ImageAnalysis.Builder()
            .setTargetResolution(Size(1280, 720))
            .setBackpressureStrategy(ImageAnalysis.STRATEGY_KEEP_ONLY_LATEST).build()
        imageAnalysis.setAnalyzer(cameraExecutor, myImageAnalyzer)

        processCameraProvider.bindToLifecycle(
            scannerActivity,
            cameraSelector,
            preview,
            imageCapture,
            imageAnalysis
        )
    }


    class MyImageAnalyzer(var resultReaded: Boolean) :
        ImageAnalysis.Analyzer {


        @RequiresApi(Build.VERSION_CODES.LOLLIPOP)
        override fun analyze(image: ImageProxy) {
            scanBarcodes(image)
        }


        @RequiresApi(Build.VERSION_CODES.LOLLIPOP)
        @SuppressLint("UnsafeOptInUsageError")
        fun scanBarcodes(image: ImageProxy) {
            val image1 = image.image!!
            val inputImage = InputImage.fromMediaImage(image1, image.imageInfo.rotationDegrees)

            // [START set_detector_options]
            val options = BarcodeScannerOptions.Builder()
                .setBarcodeFormats(
                    Barcode.FORMAT_QR_CODE,
                    Barcode.FORMAT_AZTEC
                )
                .build()
            // [END set_detector_options]

            // [START get_detector]
            val scanner = BarcodeScanning.getClient()
            // Or, to specify the formats to recognize:
            // BarcodeScanner scanner = BarcodeScanning.getClient(options);
            // [END get_detector]

            // [START run_detector]
            val result: Task<List<Barcode>> = scanner.process(inputImage)
                .addOnSuccessListener { barcodes ->
                    readerBarcodeData(barcodes)
                }
                .addOnFailureListener {
                    // Task failed with an exception
                    // ...
                }
                .addOnCompleteListener {
                    image.close()
                }
            // [END run_detector]
        }

        private fun readerBarcodeData(barcodes: List<Barcode>) {
            // Task completed successfully
            // [START_EXCLUDE]
            // [START get_barcodes]
            if (barcodes.isEmpty() || resultReaded) return
            var srr: String = ""
            for (barcode in barcodes) {
                val bounds: Rect? = barcode.boundingBox
                val corners: Array<Point>? = barcode.cornerPoints
                val rawValue = barcode.rawValue
                val valueType = barcode.valueType
                when (valueType) {
                    Barcode.TYPE_WIFI -> {
                        val ssid = barcode.wifi!!.ssid
                        val password = barcode.wifi!!.password
                        val type = barcode.wifi!!.encryptionType
                    }
                    Barcode.TYPE_URL -> {
                        srr += barcode.url?.url.toString()
                        //val title = barcode.url!!.title
                        //val url = barcode.url!!.url
                    }
                    Barcode.TYPE_TEXT -> {
                        srr += rawValue
                    }
                    Barcode.TYPE_PRODUCT -> {
                        srr += rawValue
                    }
                }
            }
            if (srr != "") {
                resultReaded = true
                Toast.makeText(scannerActivity, srr, Toast.LENGTH_LONG).show()
                val intent = Intent()
                intent.putExtra("result", srr)
                scannerActivity.setResult(Activity.RESULT_OK, intent)
                scannerActivity.finish()
            }

            // [END get_barcodes]
            // [END_EXCLUDE]
        }

    }

}