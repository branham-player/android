package tech.oliver.branhamplayer.android.startup

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import org.koin.android.ext.android.inject
import org.koin.core.parameter.parametersOf
import org.koin.standalone.StandAloneContext
import tech.oliver.branhamplayer.android.startup.shared.startupModule

class StartupActivity : AppCompatActivity() {

    private val implementation: StartupActivityImpl by inject { parametersOf(this) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        StandAloneContext.loadKoinModules(startupModule)
        implementation.onCreate(savedInstanceState)
    }
}
