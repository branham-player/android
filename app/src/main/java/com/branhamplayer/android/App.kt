package com.branhamplayer.android

import android.app.Application
import com.branhamplayer.android.utils.RemoteConfig
import com.orhanobut.logger.AndroidLogAdapter
import com.orhanobut.logger.Logger

@Suppress("unused")
class App : Application() {

    override fun onCreate() {
        super.onCreate()

        Logger.addLogAdapter(AndroidLogAdapter())
        RemoteConfig.initializeInstance()
        //Timber.plant(LogglyTree(BuildConfig.LOGGLY_KEY))
    }
}
