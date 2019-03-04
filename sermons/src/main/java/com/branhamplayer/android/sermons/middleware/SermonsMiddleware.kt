package com.branhamplayer.android.sermons.middleware

import com.branhamplayer.android.sermons.actions.PermissionAction
import com.branhamplayer.android.sermons.actions.ProfileAction
import com.branhamplayer.android.sermons.di.DaggerInjector
import com.branhamplayer.android.sermons.states.SermonsState
import org.rekotlin.DispatchFunction
import org.rekotlin.Middleware
import javax.inject.Inject

class SermonsMiddleware : Middleware<SermonsState> {

    @Inject
    lateinit var permissionMiddleware: PermissionMiddleware

    @Inject
    lateinit var profileMiddleware: ProfileMiddleware

    override fun invoke(
        dispatch: DispatchFunction,
        getState: () -> SermonsState?
    ): (DispatchFunction) -> DispatchFunction = { next ->
        { action ->
            when (action) {
                is PermissionAction -> {
                    inject()
                    permissionMiddleware.invoke(dispatch, action, getState())
                }

                is ProfileAction -> {
                    inject()
                    profileMiddleware.invoke(dispatch, action, getState())
                }
            }

            next(action)
        }
    }

    private fun inject() {
        DaggerInjector.sermonsComponent?.inject(this)
    }
}