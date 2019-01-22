package tech.oliver.branhamplayer.android

import android.app.Application
import com.orhanobut.logger.AndroidLogAdapter
import com.orhanobut.logger.Logger
import org.koin.android.ext.android.startKoin
import tech.oliver.branhamplayer.android.services.firebase.RemoteConfigService
import tech.oliver.branhamplayer.android.shared.sharedModule

@Suppress("unused")
class App : Application() {
    override fun onCreate() {
        super.onCreate()

        Logger.addLogAdapter(AndroidLogAdapter())
        RemoteConfigService.initializeInstance()
        //Timber.plant(LogglyTree(BuildConfig.LOGGLY_KEY))
        startKoin(applicationContext, sharedModule)
    }
}
