package com.branhamplayer.android.sermons.middleware

import com.branhamplayer.android.base.redux.TypedMiddleware
import com.branhamplayer.android.sermons.actions.ProfileAction
import com.branhamplayer.android.sermons.di.RxJavaModule
import com.branhamplayer.android.sermons.states.SermonsState
import com.branhamplayer.android.utils.auth0.ProfileManager
import io.reactivex.Scheduler
import org.rekotlin.DispatchFunction
import javax.inject.Inject
import javax.inject.Named

class ProfileMiddleware @Inject constructor(
    private val profileManager: ProfileManager,
    @Named(RxJavaModule.BG) private val bg: Scheduler,
    @Named(RxJavaModule.UI) private val ui: Scheduler
) : TypedMiddleware<ProfileAction, SermonsState> {

    override fun invoke(dispatch: DispatchFunction, action: ProfileAction, oldState: SermonsState?) {
        when (action) {
            is ProfileAction.GetUserProfileAction -> getUserProfile(dispatch)
        }
    }

    private fun getUserProfile(dispatch: DispatchFunction) {
        // TODO: Use a disposable
        profileManager.getUserProfileInformation()
            .subscribeOn(bg)
            .observeOn(ui)
            .subscribe({ profile ->
                dispatch(ProfileAction.SaveUserProfileAction(profile))
            }, {
                // TODO: Do nothing?
            })
    }
}
