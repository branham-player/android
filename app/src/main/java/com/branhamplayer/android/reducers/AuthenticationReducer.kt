package com.branhamplayer.android.reducers

import com.auth0.android.authentication.storage.CredentialsManager
import com.branhamplayer.android.actions.AuthenticationAction
import com.branhamplayer.android.base.redux.TypedReducer
import com.branhamplayer.android.di.DaggerInjector
import com.branhamplayer.android.states.StartupState
import javax.inject.Inject

class AuthenticationReducer : TypedReducer<AuthenticationAction, StartupState> {

    @Inject
    @JvmField
    var userCredentials: CredentialsManager? = null

    override fun invoke(action: AuthenticationAction, oldState: StartupState): StartupState {
        when (action) {
            is AuthenticationAction.SaveCredentialsAction -> saveCredentials(action)
        }

        return oldState
    }

    private fun saveCredentials(action: AuthenticationAction.SaveCredentialsAction) {
        inject()
        userCredentials?.saveCredentials(action.credentials)
    }

    private fun inject() {
        DaggerInjector.authenticationComponent?.inject(this)
    }
}
