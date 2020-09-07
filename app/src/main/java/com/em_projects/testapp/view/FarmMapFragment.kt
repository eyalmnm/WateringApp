package com.em_projects.testapp.view

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProviders
import com.em_projects.testapp.R
import com.em_projects.testapp.view.base.ScopedFragment
import com.em_projects.testapp.viewmodel.FarmMapViewModelFactory
import com.em_projects.testapp.viewmodel.FramMapViewModel
import org.kodein.di.KodeinAware
import org.kodein.di.android.x.closestKodein
import org.kodein.di.generic.instance

class FarmMapFragment : ScopedFragment(), KodeinAware {

    // Kodein Dependency Injection
    override val kodein by closestKodein()

    // Live Data Components
    private val viewModelFactory: FarmMapViewModelFactory by instance()
    private lateinit var viewModel: FramMapViewModel

    private var farmId: Int? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        farmId = arguments?.getInt("farmId")
        Toast.makeText(activity, "Farm Id: $farmId", Toast.LENGTH_SHORT).show()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fram_map_fragment, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        (activity as? AppCompatActivity)?.supportActionBar?.title = "Farm id: $farmId"
        (activity as? AppCompatActivity)?.supportActionBar?.subtitle = "click on map for create your farm"
        viewModel = ViewModelProviders.of(this, viewModelFactory).get(FramMapViewModel::class.java)
        // TODO: Use the ViewModel
    }

}