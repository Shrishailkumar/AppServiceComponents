package com.android.appcomponents.util

import android.app.ActivityManager
import android.content.Context
import android.content.pm.ApplicationInfo
import android.content.pm.PackageInfo
import android.os.BatteryManager
import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.lifecycle.MutableLiveData
import com.android.appcomponents.AppContext.AppContext
import com.android.appcomponents.model.DeviceInfo
import java.lang.Exception
import java.sql.Timestamp
import java.text.DecimalFormat
import java.text.SimpleDateFormat
import java.util.*

object DeviceInfoUtility {

     fun getDeviceInfo(): MutableLiveData<DeviceInfo> {

         val deviceInfo = MutableLiveData<DeviceInfo>()

        val deviceData = DeviceInfo(
            Build.VERSION.SDK, Build.BOARD, Build.FINGERPRINT, Build.HOST, Build.USER,
            Build.TYPE, Build.BRAND, Build.MANUFACTURER, Build.ID, Build.MODEL, Build.SERIAL,
            Build.DEVICE,Build.PRODUCT,Build.BOOTLOADER,Build.DISPLAY,Build.HARDWARE
        )

        deviceInfo.postValue(deviceData)

        return deviceInfo
    }


    private fun getTimeStamp(): String? {
        // 2021-03-24 16:48:05
        var timeStampRes = ""
        try {
            val sdf3 = SimpleDateFormat("yyyy-MM-dd HH:mm:ss")
            val timestamp = Timestamp(System.currentTimeMillis())
            timeStampRes = sdf3.format(timestamp)
            println(timeStampRes)
        } catch (e: Exception) {
            println(" exception $e")
        }
        return timeStampRes
    }

    fun getRamDetails(): String? {
        val actManager = AppContext.Companion.getContext()?.getSystemService(Context.ACTIVITY_SERVICE) as ActivityManager
        val memInfo = ActivityManager.MemoryInfo()
        actManager.getMemoryInfo(memInfo)
        val size = memInfo.totalMem
        val Kb: Long = 1024
        val Mb = Kb * 1024
        val Gb = Mb * 1024
        val Tb = Gb * 1024
        val Pb = Tb * 1024
        val Eb = Pb * 1024
        if (size < Kb) return floatForm(size.toDouble()) + " byte"
        if (size >= Kb && size < Mb) return floatForm(size.toDouble() / Kb) + " KB"
        if (size >= Mb && size < Gb) return floatForm(size.toDouble() / Mb) + " MB"
        if (size >= Gb && size < Tb) return floatForm(size.toDouble() / Gb) + " GB"
        if (size >= Tb && size < Pb) return floatForm(size.toDouble() / Tb) + " TB"
        if (size >= Pb && size < Eb) return floatForm(size.toDouble() / Pb) + " Pb"
        return if (size >= Eb) floatForm(size.toDouble() / Eb) + " Eb" else "0"
    }

    private fun floatForm(d: Double): String {
        return String.format(Locale.US, "%.2f", d)
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    fun getBateryPercentage(): String? {
        val bm = AppContext.Companion.getContext()?.getSystemService(Context.BATTERY_SERVICE) as BatteryManager
        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            val percentage = bm.getIntProperty(BatteryManager.BATTERY_PROPERTY_CAPACITY)
            "$percentage%"
        } else " %"
    }

    fun installedApps(): List<String>? {
        val listOfApps = ArrayList<String>()
        val packList: List<PackageInfo> = AppContext.Companion.getContext()?.packageManager!!.getInstalledPackages(0)
        for (i in packList.indices) {
            val packInfo = packList[i]
            if (packInfo.applicationInfo.flags and ApplicationInfo.FLAG_SYSTEM == 0) {
                val appName = packInfo.applicationInfo.loadLabel(AppContext.Companion.getContext()?.packageManager!!).toString()
                listOfApps.add(appName)
                Log.e("App № " + Integer.toString(i), appName)
            }
        }
        return listOfApps
    }

}