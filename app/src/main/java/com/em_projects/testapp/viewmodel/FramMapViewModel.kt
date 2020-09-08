package com.em_projects.testapp.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.em_projects.testapp.data.entity.FarmData
import com.em_projects.testapp.data.repository.FarmsRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class FramMapViewModel(private val farmsRepository: FarmsRepository) : ViewModel() {
    var isLoading: MutableLiveData<Boolean> = MutableLiveData()

    suspend fun updateFarmData(index: Int, farmData: FarmData): Boolean {
        withContext(Dispatchers.Main) {
            isLoading.value = true
        }
        val response = farmsRepository.updateFarmData(index, farmData)
        withContext(Dispatchers.Main) {
            isLoading.value = false
        }
        return response
    }

}