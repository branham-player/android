package com.branhamplayer.android.sermons.middleware

import com.branhamplayer.android.sermons.actions.AuthAction
import com.branhamplayer.android.sermons.actions.SermonListAction
import com.branhamplayer.android.sermons.di.DaggerInjector
import com.branhamplayer.android.sermons.states.SermonsState
import org.rekotlin.DispatchFunction
import org.rekotlin.Middleware
import javax.inject.Inject

class SermonsMiddleware : Middleware<SermonsState> {

    // region DI

    @Inject
    lateinit var authMiddleware: AuthMiddleware

    @Inject
    lateinit var sermonsListMiddleware: SermonsListMiddleware

    // endregion

    // region Middleware

    override fun invoke(
        dispatch: DispatchFunction,
        getState: () -> SermonsState?
    ): (DispatchFunction) -> DispatchFunction = { next ->
        { action ->
            when (action) {
                is AuthAction -> {
                    inject()
                    authMiddleware.invoke(dispatch, action, getState())
                }

                is SermonListAction -> {
                    inject()
                    sermonsListMiddleware.invoke(dispatch, action, getState())
                }
            }

            next(action)
        }
    }

    // endregion

    private fun inject() {
        DaggerInjector.sermonsComponent?.inject(this)
    }
}
