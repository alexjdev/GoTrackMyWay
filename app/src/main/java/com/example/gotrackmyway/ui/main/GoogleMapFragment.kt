package com.example.gotrackmyway.ui.main

import android.graphics.Color
import androidx.lifecycle.ViewModelProviders
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer

import com.example.gotrackmyway.R
import com.example.gotrackmyway.data.db.ExtLatLngEntry
import com.example.gotrackmyway.ui.ScopedFragment
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import com.google.android.gms.maps.model.PolylineOptions
//import com.example.gotrackmyway.ui.main.GoogleMapFragmentArgs
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import org.kodein.di.KodeinAware
import org.kodein.di.android.x.closestKodein
import org.kodein.di.generic.factory
import org.kodein.di.generic.instance
import java.lang.Thread.sleep





class GoogleMapFragment: ScopedFragment(), KodeinAware {

    override val kodein by closestKodein()

    private val viewModelFactoryInstanceFactory
            : ((Int) -> GoogleMapViewModelFactory) by factory()

    private lateinit var viewModel: GoogleMapViewModel

    private val LOG_TAG = this.javaClass.simpleName

    var wayID = 0

    lateinit var mapFragment : SupportMapFragment
    private lateinit var gMap: GoogleMap


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {

        val v = inflater.inflate(R.layout.google_map_fragment, container, false)

        mapFragment = childFragmentManager.findFragmentById(R.id.googleMap) as SupportMapFragment

        return v
    }


    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)



        val safeArgs = arguments?.let { GoogleMapFragmentArgs.fromBundle(it) }
        wayID = safeArgs?.wayId!!

        Log.d(LOG_TAG,"onActivityCreated wayID= $wayID")

        viewModel = ViewModelProviders.of(this, viewModelFactoryInstanceFactory(wayID!!))
            .get(GoogleMapViewModel::class.java)

        Log.d(LOG_TAG,"onActivityCreated")
        bindUI()
    }

    //TODO: If too many points -> draw it all in thread
    private fun paintPointsOnMap(pointsList: List<ExtLatLngEntry>){
        Log.d(LOG_TAG,"++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++")
        Log.d(LOG_TAG,"paintPointsOnMap pointsList.size = ${pointsList.size}")
        mapFragment.getMapAsync(OnMapReadyCallback {
            gMap = it

            gMap.clear()

            if(pointsList.isNotEmpty()) {
                val myStartLocationPoint = LatLng(pointsList.get(0).latitude, pointsList.get(0).longitude)
                gMap.addMarker(MarkerOptions().position(myStartLocationPoint).title("Start here"))
                gMap.animateCamera(CameraUpdateFactory.newLatLngZoom(myStartLocationPoint, 12f))

                val myFinishLocationPoint = LatLng(pointsList.get(pointsList.size - 1).latitude, pointsList.get(pointsList.size - 1).longitude)
                gMap.addMarker(MarkerOptions().position(myFinishLocationPoint).title("Finish here"))
            }

            if(pointsList.size >= 2) {
                Log.d(LOG_TAG,"paintPointsOnMap DRAW_ROUTE...pointsList.size ${pointsList.size}")

                val lineoption = PolylineOptions()
                for (i in pointsList.indices){
                    lineoption.add(LatLng(pointsList.get(i).latitude, pointsList.get(i).longitude))
                    lineoption.width(10f)
                    lineoption.color(Color.BLUE)
                    lineoption.geodesic(true)
                }
                gMap.addPolyline(lineoption)
            }

        })
        Log.d(LOG_TAG,"++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++")
    }

    private fun bindUI() = launch(Dispatchers.Main) {

        Log.d(LOG_TAG,"bindUI")

        val waysByIdList = viewModel.waysIdList.await()
//        Log.d(LOG_TAG,"bindUI waysByIdList.size = ${waysByIdList.value!!.size}")


        val locationsList = viewModel.getLocationsById(wayID)    //.coinResponse.await()

//        sleep(3000)

        Log.d(LOG_TAG,"bindUI locationsList.size = ${locationsList.size}")

        val allLocationsList = viewModel.getAllLocationsOrderByTime()
        Log.d(LOG_TAG,"bindUI getAllLocationsOrderByTime.size = ${allLocationsList.size}")

        waysByIdList.observe(this@GoogleMapFragment, Observer {
            if (it == null) return@Observer

            Log.d(LOG_TAG,"bindUI waysByIdList.observe.size = ${waysByIdList.value!!.size}")

            paintPointsOnMap(waysByIdList.value!!)
        })

        val allRecordsList = viewModel.allRecordsList.await()
        allRecordsList.observe(this@GoogleMapFragment, Observer {
            if (it == null) return@Observer
            Log.d(LOG_TAG,"bindUI allRecordsList.observe.size = ${allRecordsList.value!!.size}")
//            printArray(allRecordsList.value!!, "allRecordsListFromDB")
            ///paintPointsOnMap(allRecordsList.value!!)
        })

    }


    private fun printArray(printList: List<ExtLatLngEntry>, name: String) {
        Log.d(LOG_TAG,"printArray neme: $name")
        Log.d(LOG_TAG,"printArray neme: $name printList.size= ${printList.size}")
        printList.forEach {
            println(it)
        }
    }


    fun getDirectionURL(origin:LatLng,dest:LatLng) : String{
        return "https://maps.googleapis.com/maps/api/directions/json?origin=${origin.latitude},${origin.longitude}&destination=${dest.latitude},${dest.longitude}&sensor=false&mode=driving"
    }


    override fun onDestroy() {
        super.onDestroy()
        //
        //TODO:Clear resource if necessary
    }
}
