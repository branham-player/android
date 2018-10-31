package tech.oliver.branhamplayer.android

import android.app.Application
import com.github.tony19.timber.loggly.LogglyTree
import com.orhanobut.logger.AndroidLogAdapter
import com.orhanobut.logger.Logger
import org.rekotlin.Store
import tech.oliver.branhamplayer.android.reducers.appReducer
import tech.oliver.branhamplayer.android.services.firebase.RemoteConfigService
import timber.log.Timber

val store = Store(
        reducer = ::appReducer,
        state = null
)

@Suppress("unused")
class App : Application() {
    override fun onCreate() {
        super.onCreate()

        Logger.addLogAdapter(AndroidLogAdapter())
        RemoteConfigService.initializeInstance()
        Timber.plant(LogglyTree(BuildConfig.LOGGLY_KEY))
    }
}
