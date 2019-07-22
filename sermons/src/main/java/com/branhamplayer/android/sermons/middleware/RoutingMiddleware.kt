package com.branhamplayer.android.sermons.middleware

import android.content.Intent
import com.branhamplayer.android.base.redux.TypedMiddleware
import com.branhamplayer.android.sermons.actions.RoutingAction
import com.branhamplayer.android.sermons.states.SermonsState
import com.branhamplayer.android.sermons.ui.SermonsActivity
import org.rekotlin.DispatchFunction
import javax.inject.Inject

class RoutingMiddleware @Inject constructor(
    private val activity: SermonsActivity,
    private val appSettingsIntent: Intent
) : TypedMiddleware<RoutingAction, SermonsState> {

    override fun invoke(dispatch: DispatchFunction, action: RoutingAction, oldState: SermonsState?) {
        when (action) {
            is RoutingAction.NavigateToApplicationSettings -> navigateToApplicationSettings()
        }
    }

    private fun navigateToApplicationSettings() {
        activity.startActivity(appSettingsIntent)
    }
}
