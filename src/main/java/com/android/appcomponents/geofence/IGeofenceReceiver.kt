package com.android.appcomponents.geofence

interface IGeofenceReceiver {
    fun onGeofenceEnterDwell(message: String)
    fun onGeofenceExit(message: String)
    fun onGeofenceError(message: String)
}