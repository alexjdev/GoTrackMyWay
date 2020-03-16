package com.example.gotrackmyway.ui.actions

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.gotrackmyway.data.provider.PreferenceProviderImpl
import com.example.gotrackmyway.data.repository.LocationRepository


class ActionsViewModelFactory(

    private val preferenceProvider: PreferenceProviderImpl,

    private val locRepository: LocationRepository

) : ViewModelProvider.NewInstanceFactory() {

    private val LOG_TAG = this.javaClass.simpleName

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
//        Log.d(LOG_TAG,"ViewModel created")
        return ActionsViewModel(preferenceProvider, locRepository) as T
    }
}