package com.android.appcomponents.views


import android.Manifest
import android.annotation.SuppressLint
import android.app.Activity
import android.content.pm.PackageManager
import android.graphics.Point
import android.graphics.Rect
import android.os.Build
import android.os.Bundle
import android.util.Size
import android.widget.Toast
import androidx.annotation.RequiresApi
import com.android.appcomponents.R
import androidx.appcompat.app.AppCompatActivity
import androidx.camera.core.*
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.camera.view.PreviewView
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.google.mlkit.vision.barcode.BarcodeScannerOptions
import com.google.mlkit.vision.barcode.BarcodeScanning
import com.google.mlkit.vision.barcode.common.Barcode

import com.google.android.gms.tasks.Task;
import com.google.common.util.concurrent.ListenableFuture
import com.google.mlkit.vision.common.InputImage
import java.util.concurrent.ExecutionException
import java.util.concurrent.Executors
import android.content.Intent


class ScannerActivity : AppCompatActivity() {

    private lateinit var cameraProviderFuture: ListenableFuture<ProcessCameraProvider>
    private var cameraExecutor = Executors.newSingleThreadExecutor()
    internal lateinit var previewView: PreviewView

    @RequiresApi(Build.VERSION_CODES.LOLLIPOP)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_scanner2)
        previewView = findViewById(R.id.viewFindPreview)
        cameraProviderFuture = ProcessCameraProvider.getInstance(this)
        this.window.setFlags(1024, 1024)

        cameraProviderFuture.addListener({
            try {
                if (ActivityCompat.checkSelfPermission(this, Manifest.permission.CAMERA)
                    == PackageManager.PERMISSION_DENIED
                ) {
                    ActivityCompat.requestPermissions(
                        this,
                        arrayOf(Manifest.permission.CAMERA), 101
                    )
                } else {
                    val processCameraProvider = cameraProviderFuture.get() as ProcessCameraProvider
                    bindPreview(processCameraProvider)
                }
            } catch (e: ExecutionException) {
                e.printStackTrace()
            }
        }, ContextCompat.getMainExecutor(this));


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
            bindPreview(processCameraProvider2!!)
        }
    }

    @RequiresApi(Build.VERSION_CODES.LOLLIPOP)
    private fun bindPreview(processCameraProvider: ProcessCameraProvider) {
        val preview = Preview.Builder().build()
        val cameraSelector = CameraSelector.Builder().requireLensFacing(
            CameraSelector.LENS_FACING_BACK
        ).build()
        preview.setSurfaceProvider(previewView.surfaceProvider)
        processCameraProvider.unbindAll()
        val imageCapture: ImageCapture = ImageCapture.Builder().build()
        val myImageAnalyzer = MyImageAnalyzer(this, false)
        val imageAnalysis = ImageAnalysis.Builder()
            .setTargetResolution(Size(1280, 720))
            .setBackpressureStrategy(ImageAnalysis.STRATEGY_KEEP_ONLY_LATEST).build()
        imageAnalysis.setAnalyzer(cameraExecutor, myImageAnalyzer)

        processCameraProvider.bindToLifecycle(
            this,
            cameraSelector,
            preview,
            imageCapture,
            imageAnalysis
        )
    }

    class MyImageAnalyzer(var mainActivity: ScannerActivity, var resultReaded: Boolean) :
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
                    Barcode.TYPE_PRODUCT -> {
                        srr += rawValue
                    }
                }
            }
            if (srr != "") {
                resultReaded = true
                Toast.makeText(mainActivity, srr, Toast.LENGTH_LONG).show()
                val intent = Intent()
                intent.putExtra("result", srr)
                mainActivity.setResult(Activity.RESULT_OK, intent)
                mainActivity.finish()
            }

            // [END get_barcodes]
            // [END_EXCLUDE]
        }

    }
}