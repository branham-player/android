package tech.oliver.branhamplayer.android.sermons.reducers

import com.auth0.android.result.UserProfile
import tech.oliver.branhamplayer.android.sermons.actions.ProfileAction
import tech.oliver.branhamplayer.android.sermons.states.SermonsState

class ProfileReducer {
    companion object {

        fun reduce(action: ProfileAction, oldState: SermonsState) = when (action) {
            is ProfileAction.SaveUserProfileAction -> saveUserProfile(action.userProfile, oldState)
            else -> oldState
        }

        private fun saveUserProfile(userProfile: UserProfile, oldState: SermonsState) = oldState.copy(
                profile = userProfile
        )
    }
}