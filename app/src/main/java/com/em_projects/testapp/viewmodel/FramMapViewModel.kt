package com.em_projects.testapp.viewmodel

import androidx.lifecycle.ViewModel
import com.em_projects.testapp.data.entity.GeoPoint
import com.em_projects.testapp.data.repository.FarmsRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class FramMapViewModel(private val farmsRepository: FarmsRepository) : ViewModel() {
    // TODO: Implement the ViewModel

//    suspend fun updateFarmData(index: Int, points: List<GeoPoint>) {
//        withContext(Dispatchers.Main) {
//            isLoading.value = true
//        }
//        farmsRepository.updateFarmData(index, points)
//        withContext(Dispatchers.Main) {
//            isLoading.value = false
//        }
//    }

}