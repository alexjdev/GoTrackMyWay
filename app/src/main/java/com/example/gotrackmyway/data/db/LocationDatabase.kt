package com.example.gotrackmyway.data.db

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(
    entities = [ExtLatLngEntry::class],
    version = 1
)

abstract class LocationDatabase  : RoomDatabase() {
    abstract fun locationDao(): LocationDao

    companion object {
        @Volatile private var instance: LocationDatabase? = null
        private val LOCK = Any()

        operator fun invoke(context: Context) = instance ?: synchronized(LOCK) {
            instance ?: buildDatabase(context).also { instance = it }
        }

        private fun buildDatabase(context: Context) =
            Room.databaseBuilder(context.applicationContext,
                LocationDatabase::class.java, "go_track_my_way.db")
                .build()
    }
}