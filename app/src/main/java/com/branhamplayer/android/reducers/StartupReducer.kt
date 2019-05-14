package com.branhamplayer.android.reducers

import com.branhamplayer.android.actions.AuthenticationAction
import com.branhamplayer.android.actions.PreflightChecklistAction
import com.branhamplayer.android.actions.RoutingAction
import com.branhamplayer.android.base.redux.BaseAction
import com.branhamplayer.android.dagger.DaggerInjector
import com.branhamplayer.android.states.StartupState
import org.rekotlin.Action
import org.rekotlin.Reducer
import javax.inject.Inject

class StartupReducer : Reducer<StartupState> {

    // region Dagger

    @Inject
    lateinit var authenticationReducer: AuthenticationReducer

    @Inject
    lateinit var preflightChecklistReducer: PreflightChecklistReducer

    @Inject
    lateinit var routingReducer: RoutingReducer

    // endregion

    // region Reducer

    override fun invoke(action: Action, startupState: StartupState?): StartupState {
        val oldState = startupState ?: StartupState()

        if (action is BaseAction) {
            DaggerInjector.startupComponent?.inject(this)
        }

        return when (action) {
            is AuthenticationAction -> authenticationReducer.invoke(action, oldState)
            is PreflightChecklistAction -> preflightChecklistReducer.invoke(action, oldState)
            is RoutingAction -> routingReducer.invoke(action, oldState)
            else -> oldState
        }
    }

    // endregion
}
