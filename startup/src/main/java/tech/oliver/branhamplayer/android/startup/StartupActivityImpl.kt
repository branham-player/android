package tech.oliver.branhamplayer.android.startup

import android.os.Bundle
import com.bluelinelabs.conductor.Router
import com.bluelinelabs.conductor.RouterTransaction
import org.koin.core.parameter.parametersOf
import org.koin.standalone.KoinComponent
import org.koin.standalone.inject

class StartupActivityImpl(private val activity: StartUpActivity) : KoinComponent {

    private val router: Router by inject { parametersOf(activity, activity.findViewById(R.id.start_up_container), savedInstanceState) }
    private val splashScreenRoute: RouterTransaction by inject()

    private var savedInstanceState: Bundle? = null

    fun onCreate(savedInstanceState: Bundle?) {
        this.savedInstanceState = savedInstanceState
        activity.setContentView(R.layout.start_up_activity)

        if (!router.hasRootController()) {
            router.setRoot(splashScreenRoute)
        }
    }
}
