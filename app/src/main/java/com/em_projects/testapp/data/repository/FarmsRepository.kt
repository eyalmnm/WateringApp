package com.em_projects.testapp.data.repository

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.em_projects.testapp.data.entity.FarmData
import com.em_projects.testapp.data.entity.GeoPoint
import io.paperdb.Paper
import kotlinx.coroutines.delay

class FarmsRepository {
    private val FARM_LIST_KEY = "FARM_LIST_KEY"

    private var farmsList: MutableList<FarmData>? = null


    suspend fun getFarmsList(): LiveData<List<FarmData>> {
        delay(3000) // Fake network Delay
        val mutableLiveData: MutableLiveData<List<FarmData>> = MutableLiveData()
        farmsList = Paper.book().read(FARM_LIST_KEY, null)
        if (farmsList == null) {
            farmsList = mutableListOf()
            farmsList!!.add(FarmData(0, "Moo Valley Farm", 35.8701082, -85.4082868))
            farmsList!!.add(FarmData(1, "Banana Valley Farm", 35.8701082, -85.4082868))
            farmsList!!.add(FarmData(2, "Pear Valley Farm", 35.8701082, -85.4082868))
            farmsList!!.add(FarmData(3, "Grape Valley Farm", 35.8701082, -85.4082868))
            farmsList!!.add(FarmData(4, "Orange Valley Farm", 35.8701082, -85.4082868))
            farmsList!!.add(FarmData(5, "Apple Valley Farm", 35.8701082, -85.4082868))
            Paper.book().write(FARM_LIST_KEY, farmsList)
        }
        mutableLiveData.postValue(farmsList)
        return mutableLiveData
    }

    suspend fun updateFarmData(index: Int, farmData: FarmData): Boolean {
        delay(3000) // Fake network Delay
        if (farmsList?.isNotEmpty() == true && farmsList?.size!! > index) {
            farmsList!![index] = farmData
            Paper.book().write(FARM_LIST_KEY, farmsList)
            return true
        }
        return false
    }
}

/*
https://www.google.ru/maps/place/Moo+Valley+Farm/@35.8701082,-85.4082868,805m/data=!3m1!1e3!4m8!1m2!2m1!1sfarm!3m4!1s0x0:0x9abd18699797e2bc!8m2!3d35.8702557!4d-85.4102254
 */