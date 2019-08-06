package com.branhamplayer.android.sermons.middleware

import com.branhamplayer.android.base.redux.BaseAction
import com.branhamplayer.android.sermons.actions.RoutingAction
import com.branhamplayer.android.sermons.actions.SermonListAction
import com.branhamplayer.android.sermons.dagger.DaggerInjector
import com.branhamplayer.android.sermons.states.SermonsState
import org.rekotlin.DispatchFunction
import org.rekotlin.Middleware
import javax.inject.Inject

class SermonsMiddleware : Middleware<SermonsState> {

    // region Dagger

    @Inject
    lateinit var routingMiddleware: RoutingMiddleware

    @Inject
    lateinit var sermonListMiddleware: SermonListMiddleware

    // endregion

    override fun invoke(dispatch: DispatchFunction, getState: () -> SermonsState?): (DispatchFunction) -> DispatchFunction = { next ->
        { action ->
            if (action is BaseAction) {
                DaggerInjector.sermonsComponent?.inject(this)
            }

            when (action) {
                is RoutingAction -> routingMiddleware.invoke(dispatch, action, getState())
                is SermonListAction -> sermonListMiddleware.invoke(dispatch, action, getState())
            }

            next(action)
        }
    }
}
