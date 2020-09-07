package com.em_projects.testapp.application

import android.app.Application
import com.em_projects.testapp.data.repository.FarmsRepository
import com.em_projects.testapp.viewmodel.FarmListViewModelFactory
import com.em_projects.testapp.viewmodel.FarmMapViewModelFactory
import io.paperdb.Paper
import org.kodein.di.Kodein
import org.kodein.di.KodeinAware
import org.kodein.di.android.x.androidXModule
import org.kodein.di.generic.bind
import org.kodein.di.generic.instance
import org.kodein.di.generic.provider
import org.kodein.di.generic.singleton

class TestAppApplication : Application(), KodeinAware {

    override val kodein = Kodein.lazy {
        import(androidXModule(this@TestAppApplication))

        // Bind the Repository
        bind() from singleton { FarmsRepository() }

        // Bind ViewModel Factories
        bind() from provider { FarmListViewModelFactory(instance()) }
        bind() from provider { FarmMapViewModelFactory(instance()) }
    }

    override fun onCreate() {
        super.onCreate()

        // Init Paper
        Paper.init(applicationContext)
    }
}