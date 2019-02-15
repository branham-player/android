package com.branhamplayer.android.sermons.middleware

import com.branhamplayer.android.sermons.actions.ProfileAction
import com.branhamplayer.android.sermons.shared.SermonsModuleConstants
import com.branhamplayer.android.sermons.states.SermonsState
import com.branhamplayer.android.services.auth0.Auth0Service
import io.reactivex.Scheduler
import org.koin.standalone.StandAloneContext
import org.rekotlin.DispatchFunction
import org.rekotlin.Middleware

class ProfileMiddleware : Middleware<SermonsState> {

    override fun invoke(
        dispatch: DispatchFunction,
        getState: () -> SermonsState?
    ): (DispatchFunction) -> DispatchFunction = { next ->
        { action ->
            when (action) {
                is ProfileAction.GetUserProfileAction -> getUserProfile(action, dispatch)
            }

            next(action)
        }
    }

    private fun getUserProfile(action: ProfileAction.GetUserProfileAction, dispatch: DispatchFunction) {

        val auth0: Auth0Service = StandAloneContext.getKoin().koinContext.get()
        val bg: Scheduler = StandAloneContext.getKoin().koinContext.get(SermonsModuleConstants.BG_THREAD)
        val ui: Scheduler = StandAloneContext.getKoin().koinContext.get(SermonsModuleConstants.UI_THREAD)

        // TODO: Use a disposable
        auth0.getUserProfileInformation(action.context)
            .subscribeOn(bg)
            .observeOn(ui)
            .subscribe({ profile ->
                dispatch(ProfileAction.SaveUserProfileAction(profile))
            }, {
                // TODO: Do nothing?
            })
    }
}