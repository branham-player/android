package tech.oliver.branhamplayer.android.startup

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import org.koin.android.ext.android.inject
import org.koin.android.ext.android.startKoin
import org.koin.core.parameter.parametersOf
import tech.oliver.branhamplayer.android.startup.shared.startupModule

class StartUpActivity : AppCompatActivity() {

    private val implementation: StartupActivityImpl by inject { parametersOf(this) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        startKoin(applicationContext, listOf(startupModule))
        implementation.onCreate(savedInstanceState)
    }
}
