package com.branhamplayer.android.reducers

import android.content.Intent
import com.branhamplayer.android.actions.RoutingAction
import com.branhamplayer.android.base.redux.TypedReducer
import com.branhamplayer.android.states.StartupState
import com.branhamplayer.android.ui.AuthenticationFragment
import com.branhamplayer.android.ui.PreflightChecklistFragment
import com.branhamplayer.android.ui.StartupActivity
import javax.inject.Inject

class RoutingReducer @Inject constructor(
    private val startupActivity: StartupActivity,
    private val authenticationFragment: AuthenticationFragment,
    private val preflightChecklistFragment: PreflightChecklistFragment,
    private val sermonsIntent: Intent
) : TypedReducer<RoutingAction, StartupState> {

    override fun invoke(action: RoutingAction, oldState: StartupState): StartupState {
        when (action) {
            is RoutingAction.CloseAppAction -> closeApp()
            is RoutingAction.NavigateToAuthenticationAction -> navigateToAuthentication()
            is RoutingAction.NavigateToPreflightChecklistAction -> navigateToPreflightChecklist()
            is RoutingAction.NavigateToSermonsAction -> navigateToSermons()
        }

        return oldState
    }

    private fun closeApp() {
        startupActivity.finish()
    }

    private fun navigateToAuthentication() {
        startupActivity.setFragment(authenticationFragment)
    }

    private fun navigateToPreflightChecklist() {
        startupActivity.setFragment(preflightChecklistFragment)
    }

    private fun navigateToSermons() {
        sermonsIntent.addCategory(Intent.CATEGORY_BROWSABLE)
        sermonsIntent.flags = Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK
        sermonsIntent.setPackage(startupActivity.packageName)

        startupActivity.startActivity(sermonsIntent)
    }
}
