package com.em_projects.testapp.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.em_projects.testapp.data.repository.FarmsRepository
import com.em_projects.testapp.utils.lazyDeferred

class FarmsListViewModel(private val farmsRepository: FarmsRepository) : ViewModel() {
    var isLoading: MutableLiveData<Boolean> = MutableLiveData()
    val farmsList by lazyDeferred { farmsRepository.getFarmsList() }
}