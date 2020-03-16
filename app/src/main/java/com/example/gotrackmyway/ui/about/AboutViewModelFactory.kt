package com.example.gotrackmyway.ui.about

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider

class AboutViewModelFactory : ViewModelProvider.NewInstanceFactory() {

    private val LOG_TAG = this.javaClass.simpleName

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
//        Log.d(LOG_TAG,"ViewModel created")
        return AboutViewModel() as T
    }
}