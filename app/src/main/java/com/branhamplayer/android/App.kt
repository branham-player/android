package com.branhamplayer.android

import android.app.Application
import com.branhamplayer.android.base.di.ApplicationComponent
import com.branhamplayer.android.base.di.ApplicationModule
import com.branhamplayer.android.base.di.DaggerApplicationComponent
import com.branhamplayer.android.services.firebase.RemoteConfigService
import com.branhamplayer.android.shared.startupModule
import com.orhanobut.logger.AndroidLogAdapter
import com.orhanobut.logger.Logger
import org.koin.android.ext.android.startKoin

@Suppress("unused")
class App : Application() {

    private var applicationComponent: ApplicationComponent? = null

    override fun onCreate() {
        super.onCreate()

        Logger.addLogAdapter(AndroidLogAdapter())
        RemoteConfigService.initializeInstance()
        //Timber.plant(LogglyTree(BuildConfig.LOGGLY_KEY))
        startKoin(applicationContext, startupModule)
    }

    fun getApplicationComponent(): ApplicationComponent = applicationComponent ?: DaggerApplicationComponent
        .builder()
        .applicationModule(ApplicationModule(this.applicationContext))
        .build()
}
