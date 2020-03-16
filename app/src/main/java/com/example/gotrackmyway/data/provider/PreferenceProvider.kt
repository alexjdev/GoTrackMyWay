package com.example.gotrackmyway.data.provider

import android.content.Context
import android.content.SharedPreferences
import androidx.preference.PreferenceManager

abstract class PreferenceProvider(ctx: Context) {
    private val appContext = ctx.applicationContext

    private val context: Context = ctx

    protected val preferences: SharedPreferences
        get() = PreferenceManager.getDefaultSharedPreferences(appContext)

    public fun getContext() = context
}