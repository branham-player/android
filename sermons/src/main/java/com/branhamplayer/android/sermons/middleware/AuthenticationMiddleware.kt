package com.branhamplayer.android.sermons.middleware

import com.branhamplayer.android.base.redux.TypedMiddleware
import com.branhamplayer.android.sermons.actions.AuthenticationAction
import com.branhamplayer.android.sermons.di.RxJavaModule
import com.branhamplayer.android.sermons.states.SermonsState
import com.branhamplayer.android.utils.auth0.ProfileManager
import io.reactivex.Scheduler
import org.rekotlin.DispatchFunction
import javax.inject.Inject
import javax.inject.Named

class AuthenticationMiddleware @Inject constructor(
    private val profileManager: ProfileManager,
    @Named(RxJavaModule.BG) private val bg: Scheduler,
    @Named(RxJavaModule.UI) private val ui: Scheduler
) : TypedMiddleware<AuthenticationAction, SermonsState> {

    override fun invoke(dispatch: DispatchFunction, action: AuthenticationAction, oldState: SermonsState?) {
        when (action) {
            is AuthenticationAction.GetUserAuthenticationAction -> getUserProfile(dispatch)
        }
    }

    private fun getUserProfile(dispatch: DispatchFunction) {
        // TODO: Use a disposable
        profileManager.getUserProfileInformation()
            .subscribeOn(bg)
            .observeOn(ui)
            .subscribe({ profile ->
                dispatch(AuthenticationAction.SaveUserAuthenticationAction(profile))
            }, {
                // TODO: Do nothing?
            })
    }
}
