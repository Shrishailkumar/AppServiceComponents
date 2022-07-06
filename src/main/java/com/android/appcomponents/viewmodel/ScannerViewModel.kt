package com.android.appcomponents.viewmodel

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.android.appcomponents.model.DeviceInfo
import com.android.appcomponents.util.DeviceInfoUtility

class ScannerViewModel : ViewModel() {

    fun getDeviceData():LiveData<DeviceInfo>{

        return DeviceInfoUtility.getDeviceInfo()
    }

}