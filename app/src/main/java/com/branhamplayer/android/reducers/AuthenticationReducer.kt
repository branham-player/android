package com.branhamplayer.android.reducers

import com.branhamplayer.android.actions.AuthenticationAction
import com.branhamplayer.android.base.redux.TypedReducer
import com.branhamplayer.android.states.StartupState
import javax.inject.Inject

class AuthenticationReducer @Inject constructor() : TypedReducer<AuthenticationAction, StartupState> {

    override fun invoke(action: AuthenticationAction, oldState: StartupState) = oldState
}
