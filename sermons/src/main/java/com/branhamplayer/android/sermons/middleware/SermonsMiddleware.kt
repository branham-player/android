package com.branhamplayer.android.sermons.middleware

import com.branhamplayer.android.base.redux.BaseAction
import com.branhamplayer.android.sermons.actions.AuthenticationAction
import com.branhamplayer.android.sermons.actions.SermonListAction
import com.branhamplayer.android.sermons.di.DaggerInjector
import com.branhamplayer.android.sermons.states.SermonsState
import org.rekotlin.DispatchFunction
import org.rekotlin.Middleware
import javax.inject.Inject

class SermonsMiddleware : Middleware<SermonsState> {

    @Inject
    lateinit var authenticationMiddleware: AuthenticationMiddleware

    @Inject
    lateinit var sermonListMiddleware: SermonListMiddleware

    override fun invoke(
        dispatch: DispatchFunction,
        getState: () -> SermonsState?
    ): (DispatchFunction) -> DispatchFunction = { next ->
        { action ->
            if (action is BaseAction) {
                DaggerInjector.sermonsComponent?.inject(this)
            }

            when (action) {
                is AuthenticationAction -> authenticationMiddleware.invoke(dispatch, action, getState())
                is SermonListAction -> sermonListMiddleware.invoke(dispatch, action, getState())
            }

            next(action)
        }
    }
}
