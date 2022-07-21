package com.android.appcomponents.util

import android.Manifest
import android.app.PendingIntent
import android.content.Intent
import android.content.pm.PackageManager
import android.util.Log
import androidx.core.app.ActivityCompat
import com.android.appcomponents.AppContext.AppContext
import com.android.appcomponents.geofence.GeofenceBroadcastReceiverWithoutMap
import com.google.android.gms.location.Geofence
import com.google.android.gms.location.GeofencingClient
import com.google.android.gms.location.GeofencingRequest
import com.google.android.gms.location.LocationServices
import java.util.*

object GeofenceUtility {

    lateinit var geofencingClient: GeofencingClient
    var mGeofenceList: MutableList<Geofence>? = mutableListOf()

    private fun  createGeofenceObject(geoLatitude: Double, geoLongitude: Double, geofenceRadius: Float) {
        val id = UUID.randomUUID().toString()

        mGeofenceList?.add(Geofence.Builder()
            // Set the request ID of the geofence. This is a string to identify this
            // geofence.
            .setRequestId(id)

            // Set the circular region of this geofence.
            .setCircularRegion(
                geoLatitude,
                geoLongitude,
                geofenceRadius //200f
            )
            .setExpirationDuration(Geofence.NEVER_EXPIRE)
            .setTransitionTypes(Geofence.GEOFENCE_TRANSITION_ENTER or Geofence.GEOFENCE_TRANSITION_EXIT)
            .build())
    }

    fun addGeofence(geoLatitude: Double, geoLongitude: Double, geofenceRadius: Float) {

        geofencingClient = LocationServices.getGeofencingClient(AppContext.getContext()!!)
        createGeofenceObject(geoLatitude, geoLongitude, geofenceRadius)

        if (ActivityCompat.checkSelfPermission(
                AppContext.getContext()!!,
                Manifest.permission.ACCESS_FINE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            AppContext.getContext()?.let { checkLocationPermission(it) }
        }

        geofencingClient.addGeofences(getGeofencingRequest(), geofencePendingIntent)?.run {
            addOnSuccessListener {
                // Geofences added
                Log.i("TAG", "Saving Geofence")

            }
            addOnFailureListener {
                // Failed to add geofences
                Log.i("TAG", "Failed to add geofences")
            }
        }
    }

    fun stopGeofence(){
        geofencingClient?.removeGeofences(geofencePendingIntent)?.run {
            addOnSuccessListener {
                // Geofences removed
                Log.i("TAG", "Removed Geofence")
            }
            addOnFailureListener {
                // Failed to remove geofences
                Log.i("TAG", "Failed to remove geofences")
            }
        }
    }

    private val geofencePendingIntent: PendingIntent by lazy {
        val intent = Intent(AppContext.getContext(), GeofenceBroadcastReceiverWithoutMap::class.java)
        // We use FLAG_UPDATE_CURRENT so that we get the same pending intent back when calling
        // addGeofences() and removeGeofences().
        PendingIntent.getBroadcast(AppContext.getContext(), 0, intent, PendingIntent.FLAG_UPDATE_CURRENT)
    }

    private fun getGeofencingRequest(): GeofencingRequest {
        return GeofencingRequest.Builder().apply {
            setInitialTrigger(GeofencingRequest.INITIAL_TRIGGER_ENTER)
            addGeofences(mGeofenceList)
        }.build()
    }



}