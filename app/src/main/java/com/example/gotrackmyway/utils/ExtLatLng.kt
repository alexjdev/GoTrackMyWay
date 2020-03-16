package com.example.gotrackmyway.utils

import android.util.Log
import com.google.android.gms.maps.model.LatLng

//private const val BASE_LATITUDE = 59.9389363  //latitude
//private const val BASE_LONGITUDE = 30.3141748  //longitude

/**
 * Extended LatLng
 * added data and comment, usually comment will be null
 */
data class ExtLatLng(val id: Int, val latLng: LatLng, val evtTime: Long/*, val comment: String?*/) {

    private val LOG_TAG = this.javaClass.simpleName

    public var offsetDistance = 0.0000001
        get()= field
        set(value) {
            field = value
        }

    public fun almostEqualsObject(obj: ExtLatLng) : Boolean {

//        Log.d(LOG_TAG,"almostEqualsObject LAT diff= ${Math.abs(this.latLng.latitude - obj.latLng.latitude )} LNG diff= ${Math.abs(this.latLng.longitude - obj.latLng.longitude)} IS THE SAME: ${( Math.abs(this.latLng.latitude - obj.latLng.latitude ) < offsetDistance &&
//                Math.abs(this.latLng.longitude - obj.latLng.longitude) < offsetDistance )}")

        return ( Math.abs(this.latLng.latitude - obj.latLng.latitude ) < offsetDistance &&
                Math.abs(this.latLng.longitude - obj.latLng.longitude) < offsetDistance )
    }

    override fun toString(): String {
        return "ExtLatLng id: $id, evtTime= $evtTime, latLng= ${latLng.latitude}, ${latLng.longitude}"
    }
}