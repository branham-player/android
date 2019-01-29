package com.branhamplayer.android.reducers

import com.branhamplayer.android.actions.AuthenticationAction
import com.branhamplayer.android.actions.RoutingAction
import com.branhamplayer.android.states.StartupState
import org.rekotlin.Action

class StartupReducer {
    companion object {

        fun reduce(action: Action, startupState: StartupState?): StartupState {
            val state = startupState ?: StartupState()

            return when (action) {
                is AuthenticationAction -> AuthenticationReducer.reduce(action, state)
                is RoutingAction -> RoutingReducer.reduce(action, state)
                else -> state
            }
        }
    }
}
