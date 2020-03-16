package com.example.gotrackmyway.ui.main

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.gotrackmyway.data.repository.LocationRepository

class GoogleMapViewModelFactory(

    private val way_id : Int,
    private val locRepository: LocationRepository
) : ViewModelProvider.NewInstanceFactory() {

    private val LOG_TAG = this.javaClass.simpleName

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
//        Log.d(LOG_TAG,"ViewModel created")
        return GoogleMapViewModel(way_id, locRepository) as T
    }
}