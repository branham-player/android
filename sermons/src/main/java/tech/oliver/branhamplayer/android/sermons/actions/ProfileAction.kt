package tech.oliver.branhamplayer.android.sermons.actions

import android.content.Context
import com.auth0.android.result.UserProfile
import org.rekotlin.Action

sealed class ProfileAction : Action {
    data class GetUserProfileAction(val context: Context) : ProfileAction()
    data class SaveUserProfileAction(val userProfile: UserProfile) : ProfileAction()
}
