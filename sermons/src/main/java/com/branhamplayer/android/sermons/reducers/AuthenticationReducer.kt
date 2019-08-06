package com.branhamplayer.android.sermons.reducers

import com.branhamplayer.android.auth.utils.User
import com.branhamplayer.android.base.redux.TypedReducer
import com.branhamplayer.android.sermons.actions.AuthenticationAction
import com.branhamplayer.android.sermons.states.SermonsState
import javax.inject.Inject

class AuthenticationReducer @Inject constructor(
    private val user: User
) : TypedReducer<AuthenticationAction, SermonsState> {

    override fun invoke(action: AuthenticationAction, oldState: SermonsState) = when (action) {
        is AuthenticationAction.GetUserAuthenticationAction -> getUserProfile(oldState)
    }

    private fun getUserProfile(oldState: SermonsState) = oldState.copy(
        profile = user.getProfile()
    )
}
