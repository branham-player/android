package com.branhamplayer.android.sermons.reducers

import com.auth0.android.result.UserProfile
import com.branhamplayer.android.base.redux.TypedReducer
import com.branhamplayer.android.sermons.actions.AuthenticationAction
import com.branhamplayer.android.sermons.states.SermonsState
import javax.inject.Inject

class AuthenticationReducer @Inject constructor() : TypedReducer<AuthenticationAction, SermonsState> {

    override fun invoke(action: AuthenticationAction, oldState: SermonsState) = when (action) {
        is AuthenticationAction.GetUserAuthenticationAction -> oldState
        is AuthenticationAction.SaveUserAuthenticationAction -> saveUserProfile(action.userProfile, oldState)
    }

    private fun saveUserProfile(userProfile: UserProfile, oldState: SermonsState) = oldState.copy(
        profile = userProfile
    )
}
