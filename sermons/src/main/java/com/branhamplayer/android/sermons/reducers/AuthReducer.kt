package com.branhamplayer.android.sermons.reducers

import com.branhamplayer.android.base.redux.TypedReducer
import com.branhamplayer.android.sermons.actions.AuthAction
import com.branhamplayer.android.sermons.states.SermonsState

class AuthReducer : TypedReducer<AuthAction, SermonsState> {
    override fun invoke(action: AuthAction, oldState: SermonsState) = when (action) {
        is AuthAction.SaveUserProfileAction -> saveUserProfile(oldState, action)
        else -> oldState
    }

    private fun saveUserProfile(oldState: SermonsState, action: AuthAction.SaveUserProfileAction) = oldState.copy(
        profile = action.userProfile
    )
}
