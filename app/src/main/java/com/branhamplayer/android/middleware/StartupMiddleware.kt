package com.branhamplayer.android.middleware

import com.branhamplayer.android.actions.AuthenticationAction
import com.branhamplayer.android.actions.PreflightChecklistAction
import com.branhamplayer.android.di.DaggerInjector
import com.branhamplayer.android.states.StartupState
import org.rekotlin.DispatchFunction
import org.rekotlin.Middleware
import javax.inject.Inject

class StartupMiddleware : Middleware<StartupState> {

    // region DI

    @Inject
    lateinit var authenticationMiddleware: AuthenticationMiddleware

    @Inject
    lateinit var preflightChecklistMiddleware: PreflightChecklistMiddleware

    // endregion

    // region Middleware

    override fun invoke(
        dispatch: DispatchFunction,
        getState: () -> StartupState?
    ): (DispatchFunction) -> DispatchFunction = { next ->
        { action ->
            when (action) {
                is AuthenticationAction -> {
                    inject()
                    authenticationMiddleware.invoke(dispatch, action, getState())
                }

                is PreflightChecklistAction -> {
                    inject()
                    preflightChecklistMiddleware.invoke(dispatch, action, getState())
                }
            }

            next(action)
        }
    }

    // endregion

    private fun inject() {
        DaggerInjector.startupComponent?.inject(this)
    }
}
