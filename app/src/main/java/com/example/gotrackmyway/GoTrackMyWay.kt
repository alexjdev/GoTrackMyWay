package com.example.gotrackmyway

import android.app.Application
import android.content.Context
import androidx.preference.PreferenceManager
import com.example.gotrackmyway.data.db.LocationDatabase
import com.example.gotrackmyway.data.provider.PreferenceProviderImpl
import com.example.gotrackmyway.data.repository.LocationRepository
import com.example.gotrackmyway.data.repository.LocationRepositoryImpl
import com.example.gotrackmyway.ui.about.AboutViewModelFactory
import com.example.gotrackmyway.ui.actions.ActionsViewModelFactory
import com.example.gotrackmyway.ui.main.GoogleMapViewModelFactory
import com.example.gotrackmyway.ui.settings.SettingsViewModelFactory
import com.example.gotrackmyway.ui.statistic.StatisticViewModelFactory
//import com.example.gotrackmyway.utils.TrackMyWayPreferences
import org.kodein.di.Kodein
import org.kodein.di.KodeinAware
import org.kodein.di.android.x.androidXModule
import org.kodein.di.generic.*

class GoTrackMyWay : Application(), KodeinAware {
    override val kodein = Kodein.lazy {
        import(androidXModule(this@GoTrackMyWay))

        bind() from provider { PreferenceProviderImpl(instance<Context>()) }

        bind() from singleton { LocationDatabase(instance()) }

        bind() from singleton { instance<LocationDatabase>().locationDao() }

        bind<LocationRepository>() with singleton { LocationRepositoryImpl(instance()) }




        bind() from factory { way_id: Int -> GoogleMapViewModelFactory(way_id, instance()) }

        bind() from singleton { AboutViewModelFactory() }

        bind() from singleton { StatisticViewModelFactory(instance()) }

        bind() from singleton { ActionsViewModelFactory(instance(), instance()) }

    }

    companion object {
        var way_id: Int = 0
    }

//    init {
//        way_id = 0
//    }

    override fun onCreate() {
        super.onCreate()
        PreferenceManager.setDefaultValues(this, R.xml.preferences, false)
    }

}