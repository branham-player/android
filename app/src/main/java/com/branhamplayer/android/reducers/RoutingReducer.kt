package com.branhamplayer.android.reducers

import android.content.Intent
import androidx.navigation.findNavController
import com.branhamplayer.android.R
import com.branhamplayer.android.actions.RoutingAction
import com.branhamplayer.android.base.redux.TypedReducer
import com.branhamplayer.android.di.RoutingModule
import com.branhamplayer.android.states.StartupState
import com.branhamplayer.android.ui.StartupActivity
import javax.inject.Inject
import javax.inject.Named

class RoutingReducer @Inject constructor(
    private val startupActivity: StartupActivity,
    @Named(RoutingModule.GooglePlay) private val googlePlayIntent: Intent,
    @Named(RoutingModule.Sermons) private val sermonsIntent: Intent
) : TypedReducer<RoutingAction, StartupState> {

    override fun invoke(action: RoutingAction, oldState: StartupState): StartupState {
        when (action) {
            is RoutingAction.CloseAppAction -> closeApp()
            is RoutingAction.NavigateToAuthenticationAction -> return navigateToAuthentication(oldState)
            is RoutingAction.NavigateToGooglePlayStoreAction -> navigateToGooglePlayStore()
            is RoutingAction.NavigateToSermonsAction -> navigateToSermons()
        }

        return oldState
    }

    private fun closeApp() = startupActivity.finish()

    private fun navigateToAuthentication(oldState: StartupState): StartupState {
        startupActivity
            .findNavController(R.id.startup_navigation_host)
            .navigate(R.id.action_before_to_after_preflight_checklist_navigation_graph)

        return oldState.copy(
            ranPreflightChecklistSuccessfully = true
        )
    }

    private fun navigateToGooglePlayStore() = startupActivity.startActivity(googlePlayIntent)

    private fun navigateToSermons() {
        sermonsIntent.addCategory(Intent.CATEGORY_BROWSABLE)
        sermonsIntent.flags = Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK
        sermonsIntent.setPackage(startupActivity.packageName)

        startupActivity.startActivity(sermonsIntent)
    }
}
