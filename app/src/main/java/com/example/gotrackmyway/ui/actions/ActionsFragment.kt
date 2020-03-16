package com.example.gotrackmyway.ui.actions

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import android.location.Location
import android.location.LocationManager
import android.os.Build
import androidx.lifecycle.ViewModelProviders
import android.os.Bundle
import android.provider.Settings
import android.text.Editable
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.lifecycle.Observer

import com.example.gotrackmyway.R
import com.example.gotrackmyway.ui.ScopedFragment
import kotlinx.android.synthetic.main.actions_fragment.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import org.kodein.di.KodeinAware
import org.kodein.di.android.x.closestKodein
import org.kodein.di.generic.instance


private const val PERMISSION_REQUEST = 10


class ActionsFragment: ScopedFragment(), KodeinAware {

    override val kodein by closestKodein()
    private val viewModelFactory: ActionsViewModelFactory by instance()

    private lateinit var viewModel: ActionsViewModel

    private val LOG_TAG = this.javaClass.simpleName

    private var permissions = arrayOf(Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION)

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {


        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (checkPermission(permissions)) {
                Log.d(LOG_TAG,"PERMISSION GRANTED")
                ///enableView()
            } else {
                Log.d(LOG_TAG,"REQUEST PERMISSION")
                requestPermissions(permissions, PERMISSION_REQUEST)
            }
        } else {
            ///enableView()
        }

        return inflater.inflate(R.layout.actions_fragment, container, false)

    }


    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        viewModel = ViewModelProviders.of(this, viewModelFactory)
            .get(ActionsViewModel::class.java)

//        Log.d(LOG_TAG,"onActivityCreated")
        bindUI()

    }


    private fun bindUI() = launch(Dispatchers.Main) {

        Log.d(LOG_TAG,"bindUI")

        val extLatLngEntryList = viewModel.extLatLngEntryList.await()

        extLatLngEntryList.observe(this@ActionsFragment, Observer {
            if (it == null) return@Observer

            Log.d(LOG_TAG,"bindUI extLatLngEntryList.size: ${extLatLngEntryList.value!!.size}")
            extLatLngEntryList.value!!.forEach { elem ->
                println("FROM DB $elem")
            }

            if(extLatLngEntryList.value != null && extLatLngEntryList.value!!.size > 0) {
                val lastExtLatLng =
                    extLatLngEntryList.value!!.get(extLatLngEntryList.value!!.size - 1)
                actionsDlgTVLatVal.text = lastExtLatLng.latitude.toString()
                actionsDlgTVLngVal.text = lastExtLatLng.longitude.toString()
            }

        })

        actionsDlgBtnStartTracking.setOnClickListener {
            val locationManager = getActivity()!!.getSystemService(Context.LOCATION_SERVICE) as LocationManager
            viewModel.onBtnStartTrackingClicked(locationManager)
//            actionsDlgBtnStopTracking.setEnabled(true)
        }

        actionsDlgBtnStopTracking.setOnClickListener {
            viewModel.onBtnStopTrackingClicked()
        }

    }

//    fun bindUI2() {
//        Log.d(LOG_TAG,"bindUI")
//
//        actionsDlgBtnStartTracking.setOnClickListener {
//            val locationManager = getActivity()!!.getSystemService(Context.LOCATION_SERVICE) as LocationManager
//            viewModel.onBtnStartTrackingClicked(locationManager)
////            actionsDlgBtnStopTracking.setEnabled(true)
//        }
//
//        actionsDlgBtnStopTracking.setOnClickListener {
//            viewModel.onBtnStopTrackingClicked()
//        }
//
//    }


    private fun checkPermission(permissionArray: Array<String>): Boolean {
        var allSuccess = true
        for (i in permissionArray.indices) {
            if(ContextCompat.checkSelfPermission(context!!, permissionArray[i]) == PackageManager.PERMISSION_DENIED)
                allSuccess = false
        }
        return allSuccess
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
//        Log.d(LOG_TAG,"RESULT REQUEST PERMISSION requestCode= $requestCode")
        if (requestCode == PERMISSION_REQUEST) {
            var allSuccess = true
            for (i in permissions.indices) {
                if (grantResults[i] == PackageManager.PERMISSION_DENIED) {
                    allSuccess = false
                    val requestAgain = Build.VERSION.SDK_INT >= Build.VERSION_CODES.M && shouldShowRequestPermissionRationale(permissions[i])
                    if (requestAgain) {
                        Toast.makeText(this.context, "Permission denied", Toast.LENGTH_SHORT).show()
                    } else {
                        Toast.makeText(this.context, "Go to settings and enable the permission", Toast.LENGTH_SHORT).show()
                    }
                }
            }

        }
    }


}
