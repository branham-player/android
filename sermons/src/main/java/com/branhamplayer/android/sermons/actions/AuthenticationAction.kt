package com.branhamplayer.android.sermons.actions

import com.auth0.android.result.UserProfile
import com.branhamplayer.android.base.redux.BaseAction

sealed class AuthenticationAction : BaseAction {
    object GetUserAuthenticationAction : AuthenticationAction()
    data class SaveUserAuthenticationAction(val userProfile: UserProfile) : AuthenticationAction()
}
