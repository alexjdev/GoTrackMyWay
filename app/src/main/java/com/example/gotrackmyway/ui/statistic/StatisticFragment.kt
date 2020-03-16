package com.example.gotrackmyway.ui.statistic

import androidx.lifecycle.ViewModelProviders
import android.os.Bundle
import android.text.Editable
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.navigation.Navigation
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.viewpager.widget.ViewPager

import com.example.gotrackmyway.R
import com.example.gotrackmyway.data.db.ExtLatLngEntry
import com.example.gotrackmyway.ui.ScopedFragment
import com.example.gotrackmyway.ui.statistic.StatisticFragmentDirections
import com.google.android.gms.tasks.Tasks.await
import com.google.android.material.tabs.TabLayout
import com.xwray.groupie.GroupAdapter
import com.xwray.groupie.ViewHolder
import kotlinx.android.synthetic.*
import kotlinx.android.synthetic.main.statistic_fragment.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import org.kodein.di.KodeinAware
import org.kodein.di.android.x.closestKodein
import org.kodein.di.generic.instance
import java.util.ArrayList


class StatisticFragment: ScopedFragment(), KodeinAware {

    override val kodein by closestKodein()
    private val viewModelFactory: StatisticViewModelFactory by instance()

    private lateinit var viewModel: StatisticViewModel

    private val LOG_TAG = this.javaClass.simpleName


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.statistic_fragment, container, false) ///

    }


    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        viewModel = ViewModelProviders.of(this, viewModelFactory)
            .get(StatisticViewModel::class.java)

//        Log.d(LOG_TAG,"onActivityCreated")
        bindUI()
//        printStateDB()
    }

    private fun bindUI() = launch {
        Log.d(LOG_TAG,"bindUI")

        val locList = viewModel.allLocationsList.await()

        locList.observe(this@StatisticFragment, Observer {
            if (it == null) return@Observer

            initRecyclerView(it.toLocationsItems())
        })

        statisticDlg_BtnRemoveAll.setOnClickListener {
            viewModel.onBtnRemoveAllWaysClicked()
        }

        statisticDlg_BtnRemoveById.setOnClickListener {
            if(statisticDlg_ETSelectedId.text != null && statisticDlg_ETSelectedId.text.toString() != null && !statisticDlg_ETSelectedId.text.toString().equals("")) {
                viewModel.onBtnRemoveWayByIdClicked(statisticDlg_ETSelectedId.text.toString().toInt())
            }
        }

    }

    private fun List<ExtLatLngEntry>.toLocationsItems() : List<LocationItem> {
        return this.map {
            LocationItem(it)
        }
    }

    private fun initRecyclerView(items: List<LocationItem>) {
        val groupAdapter = GroupAdapter<ViewHolder>().apply {
            addAll(items)
        }

        statisticDlg_rvLocations.apply {
            layoutManager = LinearLayoutManager(this@StatisticFragment.context)
            adapter = groupAdapter
        }

        groupAdapter.setOnItemClickListener { item, view ->
            (item as? LocationItem)?.let {
//                Log.d(LOG_TAG,"CLICKED LOCATION: ${it.locEntry.toString()}")
                showMap(item, view)
            }
        }
    }

    private fun showMap(item: LocationItem, view: View) {
//        Log.d(LOG_TAG,"showMap for: ${item.locEntry.toString()}  ${view.toString()}")

        val actionGoogleMap = StatisticFragmentDirections.actionGoogleMapFragment()
        actionGoogleMap.setWayId(item.locEntry.way_id)
        Navigation.findNavController(view).navigate(actionGoogleMap)
    }

    private fun printStateDB() {

        val allList: List<ExtLatLngEntry> = viewModel.getAllLocations()
        Log.d(LOG_TAG,"printStateDB idList_size: ${allList.size}")

        allList.forEach { elem ->
            println("FROM_B $$elem")
        }
    }

}
