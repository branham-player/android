package com.branhamplayer.android.reducers

import com.auth0.android.authentication.storage.CredentialsManager
import com.branhamplayer.android.actions.AuthenticationAction
import com.branhamplayer.android.base.redux.TypedReducer
import com.branhamplayer.android.states.StartupState
import org.koin.core.parameter.parametersOf
import org.koin.standalone.StandAloneContext

class AuthenticationReducer : TypedReducer<AuthenticationAction, StartupState> {

    override fun invoke(action: AuthenticationAction, oldState: StartupState): StartupState {
        when (action) {
            is AuthenticationAction.SaveCredentialsAction -> saveCredentials(action)
        }

        return oldState
    }

    private fun saveCredentials(action: AuthenticationAction.SaveCredentialsAction) {
        val userCredentials: CredentialsManager =
            StandAloneContext.getKoin().koinContext.get { parametersOf(action.context) }

        userCredentials.saveCredentials(action.credentials)
    }
}
