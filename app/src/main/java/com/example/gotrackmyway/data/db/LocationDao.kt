package com.example.gotrackmyway.data.db

import androidx.annotation.WorkerThread
import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

@Dao
interface LocationDao {


    @Insert(onConflict = OnConflictStrategy.REPLACE)
    @WorkerThread
    fun insertList(locationsList: List<ExtLatLngEntry>)


    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun upsert(lastMyLocationPoint : ExtLatLngEntry)

    @Query("select * from location_point")
    fun getAllLocations(): LiveData<List<ExtLatLngEntry>>

    @Query("select * from location_point order by evtTime ASC")
    fun getAllLocationsOrderByTime(): LiveData<List<ExtLatLngEntry>>

    @Query("select * from location_point where way_id = :id")
    fun getLocationsById(id: Int): LiveData<List<ExtLatLngEntry>>

    @Query("select MAX(way_id) from location_point")
    fun getMaxWayId(): Int

    @Query("select count(id) from location_point")
    fun getCountRecords(): Int

    @Query("select distinct count(way_id) from location_point")
    fun getCountStoredWays(): LiveData<List<Int>>

    @Query("delete from location_point")
    fun deleteAllWays()

    @Query("delete from location_point where way_id = :id")
    fun deleteWayById(id: Int)

}