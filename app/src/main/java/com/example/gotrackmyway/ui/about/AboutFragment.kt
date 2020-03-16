package com.example.gotrackmyway.ui.about

import androidx.lifecycle.ViewModelProviders
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil

import com.example.gotrackmyway.R
import com.example.gotrackmyway.ui.ScopedFragment
import org.kodein.di.KodeinAware
import org.kodein.di.android.closestKodein
import org.kodein.di.android.x.closestKodein
import org.kodein.di.generic.instance


class AboutFragment: ScopedFragment(), KodeinAware {

    override val kodein by closestKodein()
    private val viewModelFactory: AboutViewModelFactory by instance()

    private lateinit var viewModel: AboutViewModel

    private val LOG_TAG = this.javaClass.simpleName

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        return inflater.inflate(R.layout.about_fragment, container, false)
    }


    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        viewModel = ViewModelProviders.of(this, viewModelFactory)
            .get(AboutViewModel::class.java)

    }

}
