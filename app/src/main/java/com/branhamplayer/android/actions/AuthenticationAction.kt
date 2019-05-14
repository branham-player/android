package com.branhamplayer.android.actions

import com.auth0.android.result.Credentials
import com.branhamplayer.android.base.redux.BaseAction

sealed class AuthenticationAction : BaseAction {
    data class SaveCredentialsAction(val credentials: Credentials) : AuthenticationAction()
    object ShowLoginErrorAction : AuthenticationAction()
}
