package com.android.appcomponents.viewmodel
import androidx.lifecycle.ViewModel
import com.android.appcomponents.model.DeviceInfo
import com.android.appcomponents.util.DeviceInfoUtility
import com.android.appcomponents.util.GeofenceUtility

class GeofenceViewModel : ViewModel() {

    fun addGeofence(geoLatitude: Double, geoLongitude: Double, geofenceRadius: Float) {
        GeofenceUtility.addGeofence(geoLatitude,geoLongitude,geofenceRadius)
    }

    fun removeGeofence() {
        GeofenceUtility.stopGeofence()
    }

}