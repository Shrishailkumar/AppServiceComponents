package com.android.appcomponents.util

import android.Manifest
import android.app.Activity
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.content.IntentSender
import android.content.pm.PackageManager
import android.location.LocationManager
import android.os.Build
import android.util.Log
import android.widget.Toast
import androidx.core.app.ActivityCompat
import com.android.appcomponents.AppContext.AppContext
import com.android.appcomponents.geofence.GeofenceBroadcastReceiverWithoutMap
import com.google.android.gms.common.api.ResolvableApiException
import com.google.android.gms.location.*
import com.google.android.gms.tasks.Task
import java.util.*


object GeofenceUtility {
    val MY_PERMISSIONS_REQUEST_LOCATION = 100
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

    fun addGeofence(activity: Activity, geoLatitude: Double, geoLongitude: Double, geofenceRadius: Float) {

        geofencingClient = LocationServices.getGeofencingClient(AppContext.getContext()!!)
        createGeofenceObject(geoLatitude, geoLongitude, geofenceRadius)

        if (AppContext.getContext()?.let {
                ActivityCompat.checkSelfPermission(
                    it,
                    Manifest.permission.ACCESS_FINE_LOCATION)
            } != PackageManager.PERMISSION_GRANTED
            && AppContext.getContext()
                ?.let { ActivityCompat.checkSelfPermission(it,
                    Manifest.permission.ACCESS_COARSE_LOCATION)
                } != PackageManager.PERMISSION_GRANTED)
                {
            ActivityCompat.requestPermissions(activity,
                arrayOf(Manifest.permission.ACCESS_FINE_LOCATION), 1)

        } else if ( AppContext.getContext()
                ?.let { ActivityCompat.checkSelfPermission(it,
                    Manifest.permission.ACCESS_BACKGROUND_LOCATION)
                } != PackageManager.PERMISSION_GRANTED) {

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                ActivityCompat.requestPermissions(activity,
                    arrayOf(Manifest.permission.ACCESS_BACKGROUND_LOCATION), 2)
            }
        }

       if (checkGPSEnabled(activity)) {
           geofencingClient.addGeofences(getGeofencingRequest(), geofencePendingIntent)?.run {
               addOnSuccessListener {
                   // Geofences added
                   Log.i("TAG", "Saving Geofence")

               }
               addOnFailureListener {
                   // Failed to add geofences
                   Log.i("TAG", "Failed to add Geofence")
                   Toast.makeText(
                       AppContext.getContext(),
                       "Failed to add Geofence",
                       Toast.LENGTH_LONG
                   ).show()
               }
           }
       }

    }

    private fun checkGPSEnabled(activity: Activity): Boolean {
        val manager = activity.getSystemService(Context.LOCATION_SERVICE) as LocationManager
        if (manager.isProviderEnabled(LocationManager.GPS_PROVIDER).not()) {
            return turnOnGPS(activity)
        }

        return true
    }

    private fun turnOnGPS(activity: Activity): Boolean {
        var isGpsEnable = false

        val request = LocationRequest.create().apply {
            interval = 2000
            priority = LocationRequest.PRIORITY_HIGH_ACCURACY
        }
        val builder = LocationSettingsRequest.Builder().addLocationRequest(request)
        val client: SettingsClient = LocationServices.getSettingsClient(activity)
        val task: Task<LocationSettingsResponse> = client.checkLocationSettings(builder.build())
        task.addOnFailureListener {
            if (it is ResolvableApiException) {
                try {
                    it.startResolutionForResult(activity, 12345)
                } catch (sendEx: IntentSender.SendIntentException) {
                }
            }
        }.addOnSuccessListener {
            //here GPS is On
            isGpsEnable =  true
        }

        return isGpsEnable
    }

    fun stopGeofence(){
        geofencingClient.removeGeofences(geofencePendingIntent)?.run {
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