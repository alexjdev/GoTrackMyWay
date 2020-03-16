package com.example.gotrackmyway.data.provider

import android.content.Context
import android.util.Log


const val INTERVAL = "INTERVAL"
const val IGNORE_DUPLICATIONS = "IGNORE_DUPLICATES"


class PreferenceProviderImpl(context: Context) : PreferenceProvider(context) {

    private val LOG_TAG = this.javaClass.simpleName


    fun getBooleanByKey(key: String): Boolean {
        val value = preferences.getBoolean(key, true)
        //Log.d(LOG_TAG,"getLongByKey() value: $value")
        return value
    }

    fun getStringByKey(key: String): String {
        val value = preferences.getString(key, "")
        //Log.d(LOG_TAG,"getStringByKey() value: $value")
        if(value == null) return ""
        return value
    }

    fun getIntAsStringByKey(key: String): Int {
        val value = getStringByKey(key)
        //Log.d(LOG_TAG,"getStringByKey() value: $value")
        if(value.equals("")) return 1
        return value.toInt()
    }


    fun isIgnoreDuplicates(): Boolean {
        return getBooleanByKey(IGNORE_DUPLICATIONS)
    }

    fun getInterval(): Int {
        return getIntAsStringByKey(INTERVAL)
    }

    /**
     * FUNCTION DOESN`T WORK (String instead of Long) {<string-array name="timeIntervalValues"> ---> <long-array name="timeIntervalValues">}
     */
    fun getLongByKey(key: String): Long {
        val value = preferences.getLong(key, 33)
        Log.d(LOG_TAG,"getLongByKey() value: $value")
        return value
    }

    /**
     * FUNCTION DOESN`T WORK (String instead of Int)
     */
    fun getIntByKey(key: String): Int {
        val value = preferences.getInt(key, 32)
        Log.d(LOG_TAG,"getLongByKey() value: $value")
        return value
    }

}