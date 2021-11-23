package com.android.appcomponents.viewmodel

import android.content.Context
import android.content.DialogInterface
import android.content.Intent
import android.location.Address
import android.provider.Settings
import androidx.appcompat.app.AlertDialog
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.android.appcomponents.model.LocationData
import com.android.appcomponents.util.LocationUtility
import com.google.android.gms.tasks.CancellationToken
import com.google.android.gms.tasks.CancellationTokenSource

class LocationViewModel : ViewModel() {
    var mLocationLiveData = MutableLiveData<LocationData>()
    var mAddressLiveData = MutableLiveData<String>()





    /**
     * fetch last known location
     *
     * @param context of activity or fragment
     * @return the object of LocationData model which consist of latitude, longitude, altitude etc
     */
    fun getLocation(context: Context): LiveData<LocationData>{

        context.let {

            val util = LocationUtility(context)

            if(util.locationEnabled()) {

                mLocationLiveData = util.getLastKnownLocation()
            }        else{
                context?.let {
            var alertDialog =   AlertDialog.Builder(it)
                        .setTitle("Enable Location Service")
                        .setMessage("This app needs the Location service to be enabled")
                        .setPositiveButton(
                            "OK"
                        ) { _, _ ->
                            //Prompt the user once explanation has been shown
                            context?.startActivity(Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS));
                        }
                        .setNegativeButton(
                            "Cancel"
                        ) { dialogInterface :DialogInterface, _ ->
                            dialogInterface.dismiss()
                        }
                   .create()
                        .show()
                }
            }

                return mLocationLiveData
        }
    }

    /**
     * gets a fresher, more accurate location more consistently.
     *
     * @param context of activity or fragment
     * @param priority,One of the PRIORITY  in LocationRequest e.g  LocationRequest.QUALITY_HIGH_ACCURACY
     * @return the object of LocationData model which consist of latitude, longitude, altitude etc
     */
    fun getCurrentLocation(context: Context,priority:Int): MutableLiveData<LocationData>{


        context.let {
            val util = LocationUtility(context)

            val cancellationTokenSource = CancellationTokenSource()
            mLocationLiveData = util.getCurrentLocation(priority,cancellationTokenSource.token)
            return mLocationLiveData
        }


    }
    /**
     * gets the respective address of provided location
     *
     * @param context of activity or fragment
     * @param latitude of desirable location
     * @param longitude of desirable location
     * @return the string consist of name of the address
     */
    fun getAddressNameFromLatLong(context: Context,latitude : Double, longitude : Double):LiveData<String> {

        val util = LocationUtility(context)

         mAddressLiveData =  util.getAddressFromLocation(latitude,longitude)

        return mAddressLiveData

    }
}
