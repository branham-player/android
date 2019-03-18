package com.branhamplayer.android.sermons.middleware

import com.branhamplayer.android.base.redux.TypedMiddleware
import com.branhamplayer.android.sermons.actions.AuthAction
import com.branhamplayer.android.sermons.di.RxJavaModule
import com.branhamplayer.android.sermons.states.SermonsState
import com.branhamplayer.android.services.auth0.Auth0Service
import io.reactivex.Scheduler
import org.rekotlin.DispatchFunction
import javax.inject.Inject
import javax.inject.Named

class AuthMiddleware @Inject constructor(
    private val auth0Service: Auth0Service,
    @Named(RxJavaModule.BG) private val bg: Scheduler,
    @Named(RxJavaModule.UI) private val ui: Scheduler
) : TypedMiddleware<AuthAction, SermonsState> {

    override fun invoke(dispatch: DispatchFunction, action: AuthAction, oldState: SermonsState?) {
        when (action) {
            is AuthAction.GetUserProfileAction -> getUserProfile(dispatch)
        }
    }

    private fun getUserProfile(dispatch: DispatchFunction) {
        // TODO: Use a disposable
        auth0Service.getUserProfileInformation()
            .subscribeOn(bg)
            .observeOn(ui)
            .subscribe({ profile ->
                dispatch(AuthAction.SaveUserProfileAction(profile))
            }, {
                // TODO: Do nothing?
            })
    }
}
