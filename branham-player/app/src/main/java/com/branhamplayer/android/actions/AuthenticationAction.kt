package com.branhamplayer.android.actions

import android.app.Activity
import android.content.Context
import com.auth0.android.result.Credentials
import org.rekotlin.Action

sealed class AuthenticationAction : Action {
    data class DoLoginAction(val activity: Activity) : AuthenticationAction()
    data class SaveCredentialsAction(val context: Context, val credentials: Credentials) : AuthenticationAction()
}
