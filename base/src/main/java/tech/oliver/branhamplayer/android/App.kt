package tech.oliver.branhamplayer.android

import android.app.Application
import com.github.tony19.timber.loggly.LogglyTree
import com.orhanobut.logger.AndroidLogAdapter
import com.orhanobut.logger.Logger
import org.koin.android.ext.android.startKoin
import tech.oliver.branhamplayer.android.services.firebase.RemoteConfigService
import timber.log.Timber

@Suppress("unused")
class App : Application() {
    override fun onCreate() {
        super.onCreate()

        Logger.addLogAdapter(AndroidLogAdapter())
        RemoteConfigService.initializeInstance()
        Timber.plant(LogglyTree(BuildConfig.LOGGLY_KEY))
        startKoin(applicationContext, listOf())
    }
}
