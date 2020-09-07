package com.em_projects.testapp.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.em_projects.testapp.data.repository.FarmsRepository

class FarmMapViewModelFactory(private val farmsRepository: FarmsRepository):
    ViewModelProvider.NewInstanceFactory() {

        override fun <T : ViewModel?> create(modelClass: Class<T>): T {
            return FramMapViewModel(farmsRepository) as T
        }
}