package com.branhamplayer.android.actions

import com.auth0.android.result.Credentials
import org.rekotlin.Action

sealed class AuthenticationAction : Action {
    object DoLoginAction : AuthenticationAction()
    data class SaveCredentialsAction(val credentials: Credentials) : AuthenticationAction()
}
