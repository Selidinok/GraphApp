package com.example.graphapp

import android.app.Application
import com.example.graphapp.di.appComponent
import com.example.graphapp.remote.GraphRemoteSource
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import org.koin.android.ext.android.get
import org.koin.android.ext.android.startKoin
import timber.log.Timber

class App() : Application() {

    override fun onCreate() {
        super.onCreate()
        val debug = BuildConfig.DEBUG
        initTimber(debug)
        startKoin(
            androidContext = this,
            modules = appComponent
        )
    }

    private fun initTimber(debug: Boolean) {
        if (debug) Timber.plant(Timber.DebugTree())
    }
}