package com.example.gotrackmyway.ui.settings

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider


class SettingsViewModelFactory() : ViewModelProvider.NewInstanceFactory() {

    private val LOG_TAG = this.javaClass.simpleName

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        return SettingsViewModel() as T
    }
}