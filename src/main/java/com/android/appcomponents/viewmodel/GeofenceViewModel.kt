package com.android.appcomponents.viewmodel
import android.app.Activity
import androidx.lifecycle.ViewModel
import com.android.appcomponents.model.DeviceInfo
import com.android.appcomponents.util.DeviceInfoUtility
import com.android.appcomponents.util.GeofenceUtility

class GeofenceViewModel : ViewModel() {

    fun addGeofence(activity: Activity, geoLatitude: Double, geoLongitude: Double, geofenceRadius: Float) {
        GeofenceUtility.addGeofence(activity,geoLatitude,geoLongitude,geofenceRadius)
    }

    fun removeGeofence() {
        GeofenceUtility.stopGeofence()
    }

}