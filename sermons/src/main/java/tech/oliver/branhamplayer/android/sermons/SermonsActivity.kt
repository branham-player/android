package tech.oliver.branhamplayer.android.sermons

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import org.koin.android.ext.android.inject
import org.koin.android.ext.android.startKoin
import org.koin.core.parameter.parametersOf
import org.koin.standalone.StandAloneContext
import tech.oliver.branhamplayer.android.sermons.SermonsActivityImpl
import tech.oliver.branhamplayer.android.sermons.shared.sermonsModule

class SermonsActivity : AppCompatActivity() {

    private val implementation: SermonsActivityImpl by inject { parametersOf(this) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        StandAloneContext.loadKoinModules(sermonsModule)
        implementation.onCreate(savedInstanceState)
    }
}
