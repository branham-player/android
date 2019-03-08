package com.branhamplayer.android

import android.app.Application
import com.branhamplayer.android.services.RemoteConfigService
import com.orhanobut.logger.AndroidLogAdapter
import com.orhanobut.logger.Logger

@Suppress("unused")
class App : Application() {

    override fun onCreate() {
        super.onCreate()

        Logger.addLogAdapter(AndroidLogAdapter())
        RemoteConfigService.initializeInstance()
        //Timber.plant(LogglyTree(BuildConfig.LOGGLY_KEY))
    }
}
