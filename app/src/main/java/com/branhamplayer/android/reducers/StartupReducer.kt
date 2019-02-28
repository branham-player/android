package com.branhamplayer.android.reducers

import com.branhamplayer.android.actions.AuthenticationAction
import com.branhamplayer.android.actions.RoutingAction
import com.branhamplayer.android.di.DaggerInjector
import com.branhamplayer.android.states.StartupState
import org.rekotlin.Action
import org.rekotlin.Reducer
import javax.inject.Inject

class StartupReducer : Reducer<StartupState> {

    @Inject
    @JvmField
    var authenticationReducer: AuthenticationReducer? = null

    @Inject
    @JvmField
    var routingReducer: RoutingReducer? = null

    override fun invoke(action: Action, startupState: StartupState?): StartupState {
        val oldState = startupState ?: StartupState()

        val newState = when (action) {
            is AuthenticationAction -> {
                inject()
                authenticationReducer?.invoke(action, oldState)
            }

            is RoutingAction -> {
                inject()
                routingReducer?.invoke(action, oldState)
            }

            else -> oldState
        }

        return newState ?: oldState
    }

    private fun inject() {
        DaggerInjector.startupComponent?.inject(this)
    }
}
