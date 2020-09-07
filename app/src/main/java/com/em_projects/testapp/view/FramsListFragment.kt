package com.em_projects.testapp.view

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.core.os.bundleOf
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.NavController
import androidx.navigation.Navigation
import androidx.recyclerview.widget.LinearLayoutManager
import com.em_projects.testapp.R
import com.em_projects.testapp.data.adapters.FarmsAdapter
import com.em_projects.testapp.data.adapters.ItemClickListener
import com.em_projects.testapp.view.base.ScopedFragment
import com.em_projects.testapp.viewmodel.FarmListViewModelFactory
import com.em_projects.testapp.viewmodel.FarmsListViewModel
import kotlinx.android.synthetic.main.frams_list_fragment.*
import kotlinx.coroutines.launch
import org.kodein.di.KodeinAware
import org.kodein.di.android.x.closestKodein
import org.kodein.di.generic.instance


class FramsListFragment : ScopedFragment(), KodeinAware, ItemClickListener {

    // Kodein Dependency Injection
    override val kodein by closestKodein()

    // Navigation Component
    private lateinit var navController: NavController

    // Live Data Components
    private val viewModelFactory: FarmListViewModelFactory by instance()
    private lateinit var viewModel: FarmsListViewModel

    // Farms List Components
    private lateinit var adapter: FarmsAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.frams_list_fragment, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel =
            ViewModelProviders.of(this, viewModelFactory).get(FarmsListViewModel::class.java)

        (activity as? AppCompatActivity)?.supportActionBar?.title = "Farms List"
        initProgressBar()
        initFarmsList()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        navController = Navigation.findNavController(view)
        initRecyclerView()
    }

    private fun initProgressBar() {
        viewModel.isLoading.observe(viewLifecycleOwner, Observer {
            if (it == null) return@Observer
            if (it == true) {
                showProgressBar()
            } else {
                hideProgressBar()
            }
        })
    }

    private fun initRecyclerView() {
        adapter = FarmsAdapter(listOf(), this)
        val llm = LinearLayoutManager(this.activity)
        llm.orientation = LinearLayoutManager.VERTICAL
        farmListRecyclerView.layoutManager = llm
        farmListRecyclerView.adapter = adapter
    }

    private fun initFarmsList() = launch {
        val farmsListLiveData = viewModel.farmsList.await()
        farmsListLiveData.observe(viewLifecycleOwner, Observer {
            if (it == null) return@Observer
            // Loading the new farms into the RecyclerView
            adapter.farms = it
            adapter.notifyDataSetChanged()
            loadingGrout.visibility = View.GONE
        })
    }

    override fun onItemClick(view: View?, position: Int) {
        moveToNextScreen(position)
    }

    private fun moveToNextScreen(position: Int) {
        val bundle = bundleOf("farmId" to position)
        navController.navigate(R.id.action_framsListFragment_to_farmMapFragment, bundle)
    }

    private fun showProgressBar() {
        loadingGrout.visibility = View.VISIBLE
    }

    private fun hideProgressBar() {
        loadingGrout.visibility = View.GONE
    }
}