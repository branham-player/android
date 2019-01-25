package com.branhamplayer.android.reducers

import android.content.Context
import com.auth0.android.authentication.storage.CredentialsManager
import com.auth0.android.result.Credentials
import com.branhamplayer.android.actions.AuthenticationAction
import com.branhamplayer.android.states.StartupState
import org.koin.core.parameter.parametersOf
import org.koin.standalone.StandAloneContext

class AuthenticationReducer {
    companion object {

        fun reduce(action: AuthenticationAction, startupState: StartupState): StartupState {
            when (action) {
                is AuthenticationAction.SaveCredentialsAction -> saveCredentials(action.context, action.credentials)
            }

            return startupState
        }

        private fun saveCredentials(context: Context, credentials: Credentials) {
            val userCredentials: CredentialsManager = StandAloneContext.getKoin().koinContext.get { parametersOf(context) }
            userCredentials.saveCredentials(credentials)
        }
    }
}
