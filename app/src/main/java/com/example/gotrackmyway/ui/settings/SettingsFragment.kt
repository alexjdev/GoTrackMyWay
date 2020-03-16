package com.example.gotrackmyway.ui.settings

import android.os.Bundle
import android.util.Log
import com.example.gotrackmyway.R
import androidx.appcompat.app.AppCompatActivity
import androidx.preference.PreferenceFragmentCompat



class SettingsFragment : PreferenceFragmentCompat() {

    private val LOG_TAG = this.javaClass.simpleName

    override fun onCreatePreferences(savedInstanceState: Bundle?, rootKey: String?) {
        addPreferencesFromResource(R.xml.preferences)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        (activity as? AppCompatActivity)?.supportActionBar?.title = "Settings"
        (activity as? AppCompatActivity)?.supportActionBar?.subtitle = null
    }

}
