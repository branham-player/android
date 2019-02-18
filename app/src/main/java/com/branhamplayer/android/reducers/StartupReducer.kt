package com.branhamplayer.android.reducers

import com.branhamplayer.android.actions.AuthenticationAction
import com.branhamplayer.android.actions.RoutingAction
import com.branhamplayer.android.states.StartupState
import org.rekotlin.Action
import org.rekotlin.Reducer

class StartupReducer : Reducer<StartupState> {

    // TODO: Move these to Dagger, when implemented
    private val authenticationReducer = AuthenticationReducer()
    private val routingReducer = RoutingReducer()

    override fun invoke(action: Action, startupState: StartupState?): StartupState {
        val oldState = startupState ?: StartupState()

        return when (action) {
            is AuthenticationAction -> authenticationReducer.invoke(action, oldState)
            is RoutingAction -> routingReducer.invoke(action, oldState)
            else -> oldState
        }
    }
}
