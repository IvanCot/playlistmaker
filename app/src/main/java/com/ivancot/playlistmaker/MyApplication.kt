package com.ivancot.playlistmaker

import android.app.Application
import com.ivancot.playlistmaker.data.di.repositoryModule
import com.ivancot.playlistmaker.di.viewModelModule
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin

class MyApplication : Application() {
    override fun onCreate() {
        super.onCreate()
        startKoin {
            androidContext(this@MyApplication)
            modules(repositoryModule, viewModelModule)
        }
    }
}
