package com.example.gotrackmyway.data.repository

import android.util.Log
import androidx.lifecycle.LiveData
import com.example.gotrackmyway.data.db.ExtLatLngEntry
import com.example.gotrackmyway.data.db.LocationDao
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class LocationRepositoryImpl(
    private val locationDao: LocationDao
) : LocationRepository {

    private val LOG_TAG = this.javaClass.simpleName

    override suspend fun insertList(locationsList: List<ExtLatLngEntry>) {
        locationDao.insertList(locationsList)
//        Log.d(LOG_TAG,"insertList.size= ${locationsList.size}")
    }

    override suspend fun upsert(lastMyLocationPoint: ExtLatLngEntry) {
        locationDao.upsert(lastMyLocationPoint)
//        Log.d(LOG_TAG,"upsert lastMyLocationPoint= $lastMyLocationPoint")
    }

    override suspend fun getAllLocations(): LiveData<List<ExtLatLngEntry>> {
//        Log.d(LOG_TAG,"getAllLocations")
        return withContext(Dispatchers.IO) {
            //            Log.d(LOG_TAG,"getAllLocations locationDao.getAllLocations().size ${locationDao.getAllLocations().value!!.size}")
            return@withContext locationDao.getAllLocations()
        }
    }

    override suspend fun getAllLocationsOrderByTime(): LiveData<List<ExtLatLngEntry>> {
//        Log.d(LOG_TAG,"getAllLocationsOrderByTime")
        return withContext(Dispatchers.IO) {
            //            Log.d(LOG_TAG,"getAllLocations locationDao.getAllLocations().size ${locationDao.getAllLocations().value!!.size}")
            return@withContext locationDao.getAllLocationsOrderByTime()
        }
    }

    override suspend fun getLocationsById(id: Int): LiveData<List<ExtLatLngEntry>> {
//        Log.d(LOG_TAG,"getLocationsById id= $id list.value= ${locationDao.getLocationsById(id).value}")
        return withContext(Dispatchers.IO) {
            return@withContext locationDao.getLocationsById(id)
        }
    }

    override suspend fun getMaxWayId(): Int {
        return withContext(Dispatchers.IO) {
//            Log.d(LOG_TAG,"getMaxWayId ${locationDao.getMaxWayId()}")
            return@withContext locationDao.getMaxWayId()
        }
    }

    override suspend fun getCountRecords(): Int {
        return withContext(Dispatchers.IO) {
//            Log.d(LOG_TAG,"getCountRecords ${locationDao.getCountRecords()}")
            return@withContext locationDao.getCountRecords()
        }
    }

    override suspend fun getCountStoredWays(): LiveData<List<Int>> {
        return withContext(Dispatchers.IO) {
            //            Log.d(LOG_TAG,"getCountStoredWays ${locationDao.getCountStoredWays().value!!.size}")
            return@withContext locationDao.getCountStoredWays()
        }
    }

    override suspend fun deleteAllWays() {
//        Log.d(LOG_TAG,"deleteAllWays")
        return withContext(Dispatchers.IO) {
            return@withContext locationDao.deleteAllWays()
        }
    }

    override suspend fun deleteWayById(id: Int) {
//        Log.d(LOG_TAG,"deleteWayById id= $id")
        return withContext(Dispatchers.IO) {
            return@withContext locationDao.deleteWayById(id)
        }
    }

}