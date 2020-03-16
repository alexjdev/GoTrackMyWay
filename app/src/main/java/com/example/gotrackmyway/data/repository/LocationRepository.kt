package com.example.gotrackmyway.data.repository

import androidx.lifecycle.LiveData
import com.example.gotrackmyway.data.db.ExtLatLngEntry

interface LocationRepository {

    suspend fun insertList(locationsList: List<ExtLatLngEntry>)

    suspend fun upsert(lastMyLocationPoint : ExtLatLngEntry)

    suspend fun getAllLocations(): LiveData<List<ExtLatLngEntry>>

    suspend fun getAllLocationsOrderByTime(): LiveData<List<ExtLatLngEntry>>

    suspend fun getLocationsById(id: Int): LiveData<List<ExtLatLngEntry>>

    suspend fun getMaxWayId(): Int

    suspend fun getCountRecords(): Int

    suspend fun getCountStoredWays(): LiveData<List<Int>>

    suspend fun deleteAllWays()

    suspend fun deleteWayById(id: Int)

}