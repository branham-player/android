package tech.oliver.branhamplayer.android.startup.reducers

import org.rekotlin.Action
import tech.oliver.branhamplayer.android.startup.actions.RoutingAction
import tech.oliver.branhamplayer.android.startup.states.StartupState

class StartupReducer {
    companion object {

        fun reduce(action: Action, startupState: StartupState?): StartupState {
            var state = startupState ?: StartupState()

            when (action) {
                is RoutingAction -> {
                    state = RoutingReducer.reduce(action, state)
                }
            }

            return state
        }
    }
}
