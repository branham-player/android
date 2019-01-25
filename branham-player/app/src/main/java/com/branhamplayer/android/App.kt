package com.branhamplayer.android

import android.app.Application
import com.branhamplayer.android.services.firebase.RemoteConfigService
import com.branhamplayer.android.shared.startupModule
import com.orhanobut.logger.AndroidLogAdapter
import com.orhanobut.logger.Logger
import org.koin.android.ext.android.startKoin

@Suppress("unused")
class App : Application() {
    override fun onCreate() {
        super.onCreate()

        Logger.addLogAdapter(AndroidLogAdapter())
        RemoteConfigService.initializeInstance()
        //Timber.plant(LogglyTree(BuildConfig.LOGGLY_KEY))
        startKoin(applicationContext, startupModule)
    }
}
