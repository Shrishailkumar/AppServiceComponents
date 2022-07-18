package com.android.appcomponents.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.android.appcomponents.util.ScannerUtility

class ScannerViewModel : ViewModel() {

    fun getDeviceData():LiveData<String>{

        return ScannerUtility.getResult()
    }

}