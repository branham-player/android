package com.branhamplayer.android.middleware

import com.branhamplayer.android.actions.AuthenticationAction
import com.branhamplayer.android.di.DaggerInjector
import com.branhamplayer.android.states.StartupState
import org.rekotlin.DispatchFunction
import org.rekotlin.Middleware
import javax.inject.Inject

class StartupMiddleware : Middleware<StartupState> {

    @Inject
    @JvmField
    var authenticationMiddleware: AuthenticationMiddleware? = null

    override fun invoke(
        dispatch: DispatchFunction,
        getState: () -> StartupState?
    ): (DispatchFunction) -> DispatchFunction = { next ->
        { action ->
            val middleware = when (action) {
                is AuthenticationAction -> {
                    inject()
                    authenticationMiddleware?.invoke(dispatch, getState)
                }

                else -> null
            }

            middleware?.invoke(next)?.invoke(action)
            next(action)
        }
    }

    fun inject() {
        DaggerInjector.middlewareComponent?.inject(this)
    }
}
