package com.android.appcomponents.reciever

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.graphics.BitmapFactory
import android.os.Build
import android.util.Log
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import com.android.appcomponents.R
import com.android.appcomponents.views.GeoFenceMapActivity
import com.google.android.gms.location.Geofence
import com.google.android.gms.location.GeofenceStatusCodes
import com.google.android.gms.location.GeofencingEvent

class GeofenceBroadcastReceiver : BroadcastReceiver() {
    val CHANNEL_ID = "GFG"
    val CHANNEL_NAME = "GFG ContentWriting"
    val CHANNEL_DESCRIPTION = "GFG NOTIFICATION"
    override fun onReceive(context: Context, intent: Intent) {
        val geofencingEvent = GeofencingEvent.fromIntent(intent)
        if (geofencingEvent!!.hasError()) {
            val errorMessage = GeofenceStatusCodes
                .getStatusCodeString(geofencingEvent.errorCode)
            Log.e("TAG", errorMessage)
            return
        }

        // Get the transition type.
        val geofenceTransition = geofencingEvent.geofenceTransition

        if (geofenceTransition == Geofence.GEOFENCE_TRANSITION_ENTER
            || geofenceTransition == Geofence.GEOFENCE_TRANSITION_DWELL
        ) {
            // Toast.makeText(context, "Entered and Dwelling in the Location", Toast.LENGTH_LONG).show()
            sendNotification(context, "Entered", "Entered and Dwelling in the Location")
        } else if (geofenceTransition == Geofence.GEOFENCE_TRANSITION_EXIT) {
            // Toast.makeText(context, "Exited the Location", Toast.LENGTH_LONG).show()
            Log.i("TAG", "Showing Notification...")
            sendNotification(context, "Exited", "Exited the Location")
        } else {
            sendNotification(context, "Error", "Error")
            Log.e("TAG", "Error ")
        }
    }

    fun sendNotification(context: Context, title: String, value: String) {
        val imgBitmap = BitmapFactory.decodeResource(context.resources, R.mipmap.ic_launcher_round)
        val intent1 = Intent(context, GeoFenceMapActivity::class.java)
        val pendingIntent1 =
            PendingIntent.getActivity(context, 5, intent1, PendingIntent.FLAG_UPDATE_CURRENT)
        notificationChannel(context)
        val nBuilder = NotificationCompat.Builder(context, CHANNEL_ID)
            .setContentTitle(title)
            .setContentText(value)
            .setSmallIcon(com.google.android.material.R.drawable.ic_m3_chip_check)
            .setPriority(NotificationCompat.PRIORITY_DEFAULT)
            .setContentIntent(pendingIntent1)
            .setAutoCancel(true)
            .setLargeIcon(imgBitmap)
            .setStyle(
                NotificationCompat.BigPictureStyle()
                    .bigPicture(imgBitmap)
                    .bigLargeIcon(null)
            )
            .build()
        val nManager = NotificationManagerCompat.from(context)
        nManager.notify(1, nBuilder)
    }


    private fun notificationChannel(context: Context) {
        // check if the version is equal or greater
        // than android oreo version
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            // creating notification channel and setting
            // the description of the channel
            val channel = NotificationChannel(
                CHANNEL_ID,
                CHANNEL_NAME,
                NotificationManager.IMPORTANCE_DEFAULT
            ).apply {
                description = CHANNEL_DESCRIPTION
            }
            val notificationManager: NotificationManager =
                context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            notificationManager.createNotificationChannel(channel)
        }
    }
}