package tech.oliver.branhamplayer.android.startup.reducers

import org.rekotlin.Action
import tech.oliver.branhamplayer.android.startup.actions.AuthenticationAction
import tech.oliver.branhamplayer.android.startup.actions.RoutingAction
import tech.oliver.branhamplayer.android.startup.states.StartupState

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
