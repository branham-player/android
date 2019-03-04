package com.branhamplayer.android.sermons.actions

import com.auth0.android.result.UserProfile
import org.rekotlin.Action

sealed class ProfileAction : Action {
    object GetUserProfileAction : ProfileAction()
    data class SaveUserProfileAction(val userProfile: UserProfile) : ProfileAction()
}
