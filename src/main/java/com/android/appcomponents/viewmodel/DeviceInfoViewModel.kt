package com.android.appcomponents.viewmodel

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.android.appcomponents.model.DeviceInfo
import com.android.appcomponents.util.DeviceInfoUtility

class DeviceInfoViewModel : ViewModel() {

    val deviceInfoData = MutableLiveData<DeviceInfo>()

    fun getDeviceData():LiveData<DeviceInfo>{

        return DeviceInfoUtility.getDeviceInfo()
    }

    /**
     * Method to fetch the list of Battery percentage
     * @return String
     */
    @RequiresApi(Build.VERSION_CODES.M)
    fun getBatteryInfo(): String? {

        return DeviceInfoUtility.getBateryPercentage()
    }

    /**
     * Method to fetch the current Ram Details
     * @return : String
     */
    fun getRamDetails(): String? {

        return DeviceInfoUtility.getRamDetails()
    }

    /**
     * Method to fetch the list of applications installed in the device
     * @return List<String>
     */
    fun getInstalledApps(): List<String>? {

        return DeviceInfoUtility.installedApps()
    }

}