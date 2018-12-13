package tech.oliver.branhamplayer.android.sermons

import android.os.Bundle
import com.bluelinelabs.conductor.Router
import com.bluelinelabs.conductor.RouterTransaction
import org.koin.core.parameter.parametersOf
import org.koin.standalone.KoinComponent
import org.koin.standalone.inject

class SermonsActivityImpl(private val activity: SermonsActivity) : KoinComponent {

    private val router: Router by inject { parametersOf(activity, activity.findViewById(R.id.sermons_container), savedInstanceState) }
    private val splashScreenRoute: RouterTransaction by inject()

    private var savedInstanceState: Bundle? = null

    fun onCreate(savedInstanceState: Bundle?) {
        this.savedInstanceState = savedInstanceState
        activity.setContentView(R.layout.sermons_activity)

        if (!router.hasRootController()) {
            router.setRoot(splashScreenRoute)
        }
    }
}
