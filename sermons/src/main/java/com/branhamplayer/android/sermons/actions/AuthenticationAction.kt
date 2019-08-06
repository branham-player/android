package com.branhamplayer.android.sermons.actions

import com.branhamplayer.android.base.redux.BaseAction

sealed class AuthenticationAction : BaseAction {
    object GetUserAuthenticationAction : AuthenticationAction()
}
