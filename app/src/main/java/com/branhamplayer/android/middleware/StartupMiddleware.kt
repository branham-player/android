package com.branhamplayer.android.middleware

import com.branhamplayer.android.actions.PreflightChecklistAction
import com.branhamplayer.android.base.redux.BaseAction
import com.branhamplayer.android.dagger.DaggerInjector
import com.branhamplayer.android.states.StartupState
import org.rekotlin.DispatchFunction
import org.rekotlin.Middleware
import javax.inject.Inject

class StartupMiddleware : Middleware<StartupState> {

    // region Dagger

    @Inject
    lateinit var preflightChecklistMiddleware: PreflightChecklistMiddleware

    // endregion

    // region Middleware

    override fun invoke(
        dispatch: DispatchFunction,
        getState: () -> StartupState?
    ): (DispatchFunction) -> DispatchFunction = { next ->
        { action ->
            if (action is BaseAction) {
                DaggerInjector.startupComponent?.inject(this)
            }

            when (action) {
                is PreflightChecklistAction -> preflightChecklistMiddleware.invoke(dispatch, action, getState())
            }

            next(action)
        }
    }

    // endregion
}
