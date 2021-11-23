package com.android.appcomponents.util

import android.Manifest
import android.app.Activity
import android.content.Context
import android.content.Context.CONNECTIVITY_SERVICE
import android.content.Intent
import android.content.SharedPreferences
import android.content.pm.PackageManager
import android.location.Address
import android.location.Geocoder
import android.location.LocationManager
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.os.Build
import android.provider.Settings
import android.util.Log
import androidx.appcompat.app.AlertDialog
import androidx.core.app.ActivityCompat
import androidx.lifecycle.MutableLiveData
import com.android.appcomponents.model.LocationData
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationRequest
import com.google.android.gms.location.LocationServices
import com.google.android.gms.tasks.CancellationToken
import kotlin.collections.HashMap
import androidx.core.content.ContextCompat.getSystemService
import java.io.IOError
import java.io.IOException
import java.util.*


val MY_PERMISSIONS_REQUEST_LOCATION = 99
private lateinit var fusedLocationClient: FusedLocationProviderClient

lateinit var sharedPreferences: SharedPreferences

class LocationUtility(context: Context? = null) {

    var ctx = context
    private val TAG = LocationUtility::class.qualifiedName


    fun isNetworkConnected(): Boolean {
        val connectivityManager = ctx?.getSystemService(CONNECTIVITY_SERVICE) as ConnectivityManager
        val activeNetwork = connectivityManager.activeNetwork
        val networkCapabilities = connectivityManager.getNetworkCapabilities(activeNetwork)
        // sharedPreferences = ctx?.getSharedPreferences("cordinates", MODE_PRIVATE)!!
        return networkCapabilities != null &&
                networkCapabilities.hasCapability(NetworkCapabilities.NET_CAPABILITY_INTERNET)

    }

    fun getLastKnownLocation(): MutableLiveData<LocationData> {
        ctx?.let { checkLocationPermission(it) }

        fusedLocationClient = LocationServices.getFusedLocationProviderClient(ctx)
        var cordinatesList = getLastKnownCoordinates()
        return cordinatesList


    }

    fun getCurrentLocation(
        priority: Int,
        cancellationToken: CancellationToken
    ): MutableLiveData<LocationData> {
        ctx?.let { checkLocationPermission(it) }
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(ctx)

        var cordinatesList = getCurrentLocationWithPriority(priority, cancellationToken)
        return cordinatesList
    }


   fun getAddressNameFromLocation(latitude : Double, longitude :Double) :MutableLiveData<String>{



       ctx?.let { checkLocationPermission(it) }

       var mutableAddressName = getAddressFromLocation(latitude,longitude)

       return mutableAddressName
   }

    fun getAddressFromLocation (latitude : Double, longitude :Double) : MutableLiveData<String> {

        val mutableAddress = MutableLiveData<String>()

        val geocoder = Geocoder(ctx, Locale.ENGLISH)
        try {

            val addresses: List<Address> = geocoder.getFromLocation(latitude, longitude, 1)

            val strAddress = StringBuilder();

            if (addresses.isNotEmpty()) {
                val fetchedAddress: Address = addresses[0];

                for (i in 0..fetchedAddress.getMaxAddressLineIndex()) {
                    strAddress.append(fetchedAddress.getAddressLine(i)).append(" ");
                }

                mutableAddress.postValue(strAddress.toString())

            }

            return mutableAddress
        }
        catch (e: IOException){
            e.printStackTrace()
            return mutableAddress
        }
    }
    fun getLastKnownCoordinates(): MutableLiveData<LocationData> {
        var locationLatLonDetails = MutableLiveData<LocationData>()


        if (ctx?.let {
                ActivityCompat.checkSelfPermission(
                    it,
                    Manifest.permission.ACCESS_COARSE_LOCATION
                )
            } == PackageManager.PERMISSION_GRANTED &&
            ActivityCompat.checkSelfPermission(
                ctx!!,
                Manifest.permission.ACCESS_FINE_LOCATION
            ) == PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                ctx!!,
                Manifest.permission.ACCESS_COARSE_LOCATION
            )
            == PackageManager.PERMISSION_GRANTED
        ) {




            fusedLocationClient!!.lastLocation
                .addOnCompleteListener(ctx as Activity) { task ->
                    if (task.isSuccessful && task.result != null) {
                        task.result.latitude;
                        task.result.longitude
                        Log.d(TAG, "lat = ${task.result.latitude}")
                        Log.d(TAG, "lon = ${task.result.longitude}")
                        val locationData = LocationData(
                            task.result.latitude, task.result.longitude,
                            task.result.altitude, task.result.accuracy
                        )

                        locationLatLonDetails.postValue(locationData)

                        /* sharedPreferences = ctx.getSharedPreferences("cordinates", MODE_PRIVATE)
                         sharedPreferences.edit().putString("lat", task.result.latitude.toString())
                             ?.apply()
                         sharedPreferences.edit().putString("lon", task.result.longitude.toString())
                             ?.apply()*/
                    } else {
                        Log.w("LAST KNOWN LOCATION", "getLastLocation:exception", task.exception)
                    }
                }
        }
        return locationLatLonDetails
    }

    fun getCurrentLocationWithPriority(
        priority: Int,
        cancellationToken: CancellationToken
    ): MutableLiveData<LocationData> {

        var locationLatLonDetails = MutableLiveData<LocationData>()


        if (ctx?.let {
                ActivityCompat.checkSelfPermission(
                    it,
                    Manifest.permission.ACCESS_COARSE_LOCATION
                )
            } == PackageManager.PERMISSION_GRANTED &&
            ActivityCompat.checkSelfPermission(
                ctx!!,
                Manifest.permission.ACCESS_FINE_LOCATION
            ) == PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                ctx!!,
                Manifest.permission.ACCESS_COARSE_LOCATION
            )
            == PackageManager.PERMISSION_GRANTED
        ) {


            fusedLocationClient.getCurrentLocation(priority, cancellationToken)
                .addOnSuccessListener {


                    it?.let {

                        Log.d("Current_LOC", "lat = ${it.latitude}")
                        Log.d("Current_LOC", "lon = ${it.longitude}")
                        val locationData = LocationData(
                            it.latitude, it.longitude,
                            it.altitude, it.accuracy
                        )

                        locationLatLonDetails.postValue(locationData)

                    }

                }

        }
        return locationLatLonDetails
    }


    fun locationEnabled(): Boolean {

        val activity = ctx as Activity
        val locationManager = activity.getSystemService(Context.LOCATION_SERVICE) as LocationManager
        return locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER) || locationManager.isProviderEnabled(
            LocationManager.NETWORK_PROVIDER
        );

    }


}


private fun checkLocationPermission(ctx: Context) {

    if (ActivityCompat.checkSelfPermission(
            ctx,
            Manifest.permission.ACCESS_FINE_LOCATION
        ) != PackageManager.PERMISSION_GRANTED
    ) {
        // Should we show an explanation?
        if (ActivityCompat.shouldShowRequestPermissionRationale(
                ctx as Activity,
                Manifest.permission.ACCESS_FINE_LOCATION
            )
        ) {
            // Show an explanation to the user *asynchronously* -- don't block
            // this thread waiting for the user's response! After the user
            // sees the explanation, try again to request the permission.
            AlertDialog.Builder(ctx)
                .setTitle("Location Permission Needed")
                .setMessage("This app needs the Location permission, please accept to use location functionality")
                .setPositiveButton(
                    "OK"
                ) { _, _ ->
                    //Prompt the user once explanation has been shown
                    requestLocationPermission(ctx)
                }
                .create()
                .show()
        } else {
            // No explanation needed, we can request the permission.
            requestLocationPermission(ctx)
        }
    }
}

private fun requestLocationPermission(ctx: Context) {
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
        ActivityCompat.requestPermissions(
            ctx as Activity,
            arrayOf(
                Manifest.permission.ACCESS_FINE_LOCATION,
                Manifest.permission.ACCESS_COARSE_LOCATION,
                Manifest.permission.ACCESS_BACKGROUND_LOCATION
            ),
            MY_PERMISSIONS_REQUEST_LOCATION
        )
    } else {
        ActivityCompat.requestPermissions(
            ctx as Activity,
            arrayOf(Manifest.permission.ACCESS_FINE_LOCATION),
            MY_PERMISSIONS_REQUEST_LOCATION
        )
    }


}


// fusedLocationClient = LocationServices.getFusedLocationProviderClient(this)
