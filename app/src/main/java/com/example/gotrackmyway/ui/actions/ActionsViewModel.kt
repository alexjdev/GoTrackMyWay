package com.example.gotrackmyway.ui.actions

import android.Manifest
import android.annotation.SuppressLint
import android.content.Context
import android.content.pm.PackageManager
import android.location.Location
import android.location.LocationListener
import android.location.LocationManager
import android.os.Bundle
import android.util.Log
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.lifecycle.ViewModel
import com.example.gotrackmyway.data.provider.PreferenceProviderImpl
import com.example.gotrackmyway.utils.ExtLatLng
import com.example.gotrackmyway.utils.exceptions.LocationPermissionNotGrantedException
import com.example.gotrackmyway.utils.lazyDeferred
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationCallback
import com.google.android.gms.location.LocationResult
import com.google.android.gms.maps.model.LatLng
import okhttp3.internal.Internal.instance
import androidx.core.content.ContextCompat.getSystemService
import android.content.Context.LOCATION_SERVICE
import androidx.core.content.ContextCompat.getSystemService
import com.example.gotrackmyway.data.db.ExtLatLngEntry
import com.example.gotrackmyway.data.repository.LocationRepository
import kotlinx.coroutines.*


private const val MY_PERMISSION_ACCESS_COARSE_LOCATION = 1

class ActionsViewModel(

    private val preferenceProvider: PreferenceProviderImpl,

    private val locationRepository: LocationRepository

) : ViewModel(), LocationListener {

    private val LOG_TAG = this.javaClass.simpleName

    /**
     * For testing with out DB
     */
    companion object {

        private val trackList = ArrayList<ExtLatLng>()
        private var trackingState = false
        private var lastLocation: ExtLatLng = ExtLatLng(0, LatLng(0.0,0.0), System.currentTimeMillis())
        private var lastID = 0
        private var interval: Int = 5 //Default!!!!!
        private var ignoreDuplicate = true //Default

    }

    init {
        runBlocking {
            async { lastID = locationRepository.getMaxWayId()
                Log.d(LOG_TAG,"INIT_BLOCK lastID_FROM_DB= $lastID")
                lastID++
            }.await()
        }
    }

    val extLatLngEntryList by lazyDeferred{
        //        locationRepository.getAllLocations()
        locationRepository.getAllLocationsOrderByTime()
    }


    lateinit var locationManager: LocationManager
    private var hasGps = false
    private var locationGps: Location? = null

    override fun onLocationChanged(location: Location?) {
        Log.d(LOG_TAG,"onLocationChanged")

        if (location != null) {
            locationGps = location
            Log.d(LOG_TAG, " GPS Latitude : ${locationGps!!.latitude}")
            Log.d(LOG_TAG, " GPS Longitude : ${locationGps!!.longitude}")

            val currentExtLatLng: ExtLatLng = ExtLatLng(lastID,(LatLng(locationGps!!.latitude, locationGps!!.longitude)), System.currentTimeMillis())

            if((ignoreDuplicate && (! lastLocation.almostEqualsObject(currentExtLatLng))) || !ignoreDuplicate) {
                lastLocation = currentExtLatLng
                trackList.add(lastLocation)


                GlobalScope.launch(Dispatchers.IO) {

                    locationRepository.upsert( ExtLatLngEntry(0L, lastID, System.currentTimeMillis(), locationGps!!.latitude, locationGps!!.longitude) )

                    Log.d( LOG_TAG,"DB.getCountRecords()= ${locationRepository.getCountRecords()}")

                    Log.d( LOG_TAG,"DB.getMaxWayId()= ${locationRepository.getMaxWayId()}")

                }

                Log.d( LOG_TAG,"ADDED new location to array trackList.size= ${trackList.size}")
            } else {
                Log.d( LOG_TAG,"IGNORE DUPLICATE trackList.size= ${trackList.size}")
            }
        }

    }

    override fun onStatusChanged(provider: String?, status: Int, extras: Bundle?) {
        Log.d(LOG_TAG,"onStatusChanged")
    }

    override fun onProviderEnabled(provider: String?) {
        Log.d(LOG_TAG,"onProviderEnabled")
    }

    override fun onProviderDisabled(provider: String?) {
        Log.d(LOG_TAG,"onProviderDisabled")
    }


    fun onBtnStartTrackingClicked(lm: LocationManager) {
        Log.d(LOG_TAG,"onBtnStartTrackingClicked")
        this.locationManager = lm
        trackingState = true

        ignoreDuplicate = preferenceProvider.isIgnoreDuplicates()

        interval = preferenceProvider.getInterval()

        Log.d(LOG_TAG,"onBtnStartTrackingClicked ignoreDuplicate = ${ignoreDuplicate}")


        Log.d(LOG_TAG,"onBtnStartTrackingClicked interval = ${interval}")

        if(!hasLocationPermission()) {
            throw LocationPermissionNotGrantedException()
        }
        getLocation(/*lm*/)
    }

    fun onBtnStopTrackingClicked() {
        Log.d(LOG_TAG,"onBtnStopTrackingClicked")
        lastID++
        trackingState = false
        stopLocation()

        printArray()

        var dbRowCounter = 0

        runBlocking {
            async { dbRowCounter = locationRepository.getCountRecords() }.await()
        }

        println("NUMBER ROWS IN DB = $dbRowCounter")

        //clearDB() //for testing

    }

    private fun clearDB() {
        GlobalScope.launch(Dispatchers.IO) {

            locationRepository.deleteAllWays()

            Log.d( LOG_TAG,"DB_AFTER_DEL.getCountRecords()= ${locationRepository.getCountRecords()}")

            Log.d( LOG_TAG,"DB_AFTER_DEL.getMaxWayId()= ${locationRepository.getMaxWayId()}")

        }
    }

    private fun printArray() {
        Log.d(LOG_TAG,"printArray")
        Log.d(LOG_TAG,"printArray trackList.size= ${trackList.size}")
        trackList.forEach {
            println(it)
        }
    }

    private fun stopLocation() {
        if(locationManager != null)
            locationManager.removeUpdates(this)
        Log.d(LOG_TAG,"locationManager stopped")
    }


    @SuppressLint("MissingPermission")
    private fun getLocation() {

        hasGps = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)
        if (hasGps ) {

            if (hasGps) {
                Log.d(LOG_TAG, "hasGps")

                locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 1000L*interval, 0F, this)


                val localGpsLocation = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER)
                if (localGpsLocation != null)
                    locationGps = localGpsLocation
            }


        } else {
            Log.d( LOG_TAG,"GPS NOT FOUND")
        }
    }

    private fun hasLocationPermission(): Boolean {
        return ContextCompat.checkSelfPermission(preferenceProvider.getContext(),
            Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED
    }

//    private fun checkRequestLocationPermission() : Boolean {
//        return true
//    }

}
