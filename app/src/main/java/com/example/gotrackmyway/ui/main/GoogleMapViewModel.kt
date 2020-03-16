package com.example.gotrackmyway.ui.main

import android.util.Log
import androidx.lifecycle.ViewModel
import com.example.gotrackmyway.data.db.ExtLatLngEntry
import com.example.gotrackmyway.data.repository.LocationRepository
import com.example.gotrackmyway.utils.lazyDeferred
import kotlinx.coroutines.async
import kotlinx.coroutines.runBlocking

class GoogleMapViewModel(

    private val way_id: Int,
    private val locationRepository: LocationRepository
) : ViewModel() {

    private val LOG_TAG = this.javaClass.simpleName

    val waysIdList by lazyDeferred{
        locationRepository.getLocationsById(way_id)
    }

    val allRecordsList by lazyDeferred{
        locationRepository.getAllLocationsOrderByTime()
    }

    public fun getLocationsById(id: Int) : List<ExtLatLngEntry> {

        Log.d(LOG_TAG,"getLocationsById ${id} ")
        var ret: List<ExtLatLngEntry> = ArrayList<ExtLatLngEntry>()
        runBlocking {
            async {
                if( locationRepository.getLocationsById(id).value != null) {
                    Log.d(LOG_TAG,"getLocationsById != null size: ${locationRepository.getLocationsById(id).value!!.size} ")
                    ret = locationRepository.getLocationsById(id).value!!
                }
//                ret = locationRepository.getLocationsById(id).value!!
            }.await()
        }
        Log.d(LOG_TAG,"getLocationsById size: ${ret.size} ")
        return ret
    }



    public fun getAllLocationsOrderByTime() : List<ExtLatLngEntry> {

        Log.d(LOG_TAG,"getAllLocationsOrderByTime")
        var ret: List<ExtLatLngEntry> = ArrayList<ExtLatLngEntry>()
        runBlocking {
            async {
                if( locationRepository.getAllLocationsOrderByTime().value != null) {
                    Log.d(LOG_TAG,"getAllLocationsOrderByTime != null size: ${locationRepository.getAllLocationsOrderByTime().value!!.size} ")
                    ret = locationRepository.getAllLocationsOrderByTime().value!!
                }
//                ret = locationRepository.getLocationsById(id).value!!
            }.await()
        }
        Log.d(LOG_TAG,"getAllLocationsOrderByTime.size: ${ret.size} ")
        return ret
    }


}
