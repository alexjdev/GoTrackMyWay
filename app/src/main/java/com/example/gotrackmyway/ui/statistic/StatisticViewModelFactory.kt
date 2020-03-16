package com.example.gotrackmyway.ui.statistic

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.gotrackmyway.data.repository.LocationRepository


class StatisticViewModelFactory(

    private val locRepository: LocationRepository
) : ViewModelProvider.NewInstanceFactory() {

    private val LOG_TAG = this.javaClass.simpleName

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
//        Log.d(LOG_TAG,"ViewModel created")
        return StatisticViewModel(locRepository) as T
    }
}