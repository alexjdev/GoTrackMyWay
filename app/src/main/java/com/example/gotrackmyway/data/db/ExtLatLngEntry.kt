package com.example.gotrackmyway.data.db

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.gson.annotations.SerializedName


@Entity(tableName = "location_point")
data class ExtLatLngEntry(

    @PrimaryKey(autoGenerate = true)
    @SerializedName("id")
    val id: Long,

    @SerializedName("way_id")
    val way_id: Int,

    @SerializedName("evtTime")
    val evtTime: Long,

    @SerializedName("latitude")
    val latitude: Double,

    @SerializedName("longitude")
    val longitude: Double

) {

    companion object {
        public var offsetDistance = 0.0000001    //  public var offsetDistance = 0.00000000000001
            get()= field
            set(value) {
                field = value
            }
    }

    public fun almostEqualsObject(obj: ExtLatLngEntry) : Boolean {

//        Log.d(LOG_TAG,"almostEqualsObject LAT diff= ${Math.abs(this.latLng.latitude - obj.latLng.latitude )} LNG diff= ${Math.abs(this.latLng.longitude - obj.latLng.longitude)} IS THE SAME: ${( Math.abs(this.latLng.latitude - obj.latLng.latitude ) < offsetDistance &&
//                Math.abs(this.latLng.longitude - obj.latLng.longitude) < offsetDistance )}")

        return ( Math.abs(this.latitude - obj.latitude ) < offsetDistance &&
                Math.abs(this.longitude - obj.longitude) < offsetDistance )
    }

    override fun toString(): String {
        return "ExtLatLng id: $id, way_id= $way_id, evtTime= $evtTime, latLng= $latitude, $longitude"
    }
}