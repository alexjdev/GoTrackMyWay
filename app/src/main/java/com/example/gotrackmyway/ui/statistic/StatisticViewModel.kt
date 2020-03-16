package com.example.gotrackmyway.ui.statistic

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.example.gotrackmyway.data.db.ExtLatLngEntry
import com.example.gotrackmyway.data.repository.LocationRepository
import com.example.gotrackmyway.utils.lazyDeferred
import kotlinx.coroutines.*


class StatisticViewModel(
    private val locationRepository: LocationRepository

) : ViewModel() {

    private val LOG_TAG = this.javaClass.simpleName

    val waysIdList by lazyDeferred{
        locationRepository.getCountStoredWays()
    }

    val allLocationsList by lazyDeferred{
        locationRepository.getAllLocations()
    }

    public fun getAllLocations() : List<ExtLatLngEntry> {
        Log.d(LOG_TAG,"getAllLocations")
        var ret: List<ExtLatLngEntry> = ArrayList<ExtLatLngEntry>()
        runBlocking {
            async {
                if( locationRepository.getAllLocations().value != null) {
                    Log.d(LOG_TAG,"getAllLocations != null size: ${locationRepository.getAllLocations().value!!.size} ")
                    ret = locationRepository.getAllLocations().value!!
                }
//                ret = locationRepository.getLocationsById(id).value!!
            }.await()
        }
        Log.d(LOG_TAG,"getAllLocations size: ${ret.size} ")
        return ret
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

    public fun onBtnRemoveAllWaysClicked() {
        removeAllWays()
    }

    public fun onBtnRemoveWayByIdClicked(id: Int) {
        removeWayById(id)
    }

    private fun removeAllWays() {
        Log.d(LOG_TAG,"removeAllWays")

        runBlocking {
            async { locationRepository.deleteAllWays() }.await()
        }
    }

    private fun removeWayById(id: Int) {
        Log.d(LOG_TAG,"removeWayById ID= $id")

        runBlocking {
            async { locationRepository.deleteWayById(id) }.await()
        }
    }

}
