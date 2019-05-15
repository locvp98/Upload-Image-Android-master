package com.hoc.uploadimage

import android.app.Application
import org.koin.android.ext.android.startKoin

class MyApp : Application() {

    override fun onCreate() {
        super.onCreate()
        application = this
        startKoin(listOf(retrofitModule, appModule, viewModelModule))
    }

    companion object {
        lateinit var application: Application
            private set
    }
}
