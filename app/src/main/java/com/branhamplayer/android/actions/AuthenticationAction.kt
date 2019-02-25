package com.branhamplayer.android.actions

import android.content.Context
import com.auth0.android.result.Credentials
import org.rekotlin.Action

sealed class AuthenticationAction : Action {
    object DoLoginAction : AuthenticationAction()
    data class SaveCredentialsAction(val context: Context, val credentials: Credentials) : AuthenticationAction()
}
