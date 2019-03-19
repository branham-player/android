package com.branhamplayer.android.sermons.actions

import com.auth0.android.result.UserProfile

sealed class AuthAction : SermonsAction {
    object GetUserProfileAction : AuthAction()
    data class SaveUserProfileAction(val userProfile: UserProfile) : AuthAction()
}
