package com.android.appcomponents.geofence

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.util.Log
import com.google.android.gms.location.Geofence
import com.google.android.gms.location.GeofenceStatusCodes
import com.google.android.gms.location.GeofencingEvent

class GeofenceBroadcastReceiverWithoutMap : BroadcastReceiver() {

    companion object {
        private var iGeofenceReceiver: IGeofenceReceiver? = null

        fun registerGeofenceCallback(iGeofenceReceiver: IGeofenceReceiver) {
            Companion.iGeofenceReceiver = iGeofenceReceiver
        }
    }

    override fun onReceive(context: Context, intent: Intent) {
        val geofencingEvent = GeofencingEvent.fromIntent(intent)
        if (geofencingEvent.hasError()) {
            val errorMessage = GeofenceStatusCodes
                .getStatusCodeString(geofencingEvent.errorCode)
            Log.e("TAG", errorMessage)
            return
        }

        // Get the transition type.
        val geofenceTransition = geofencingEvent.geofenceTransition

        if (geofenceTransition == Geofence.GEOFENCE_TRANSITION_ENTER || geofenceTransition == Geofence.GEOFENCE_TRANSITION_DWELL) {
            iGeofenceReceiver?.onGeofenceEnterDwell("Entered and Dwelling in the Location")
        }else if (geofenceTransition == Geofence.GEOFENCE_TRANSITION_EXIT) {
            iGeofenceReceiver?.onGeofenceExit("Exited the Location")
        } else {
            iGeofenceReceiver?.onGeofenceError("Error")
        }
    }

}