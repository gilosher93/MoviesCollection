package com.example.moviescollection

import android.app.Application
import com.example.moviescollection.di.appModule
import com.example.moviescollection.di.moviesModule
import com.example.moviescollection.di.networkModule
import com.example.moviescollection.di.viewModelsModule
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin

class App: Application() {

    override fun onCreate() {
        super.onCreate()

        startKoin {
            androidContext(this@App)
            modules(appModule, networkModule, viewModelsModule, moviesModule)
        }
    }
}