package com.branhamplayer.android.sermons.reducers

import com.auth0.android.result.UserProfile
import com.branhamplayer.android.base.redux.TypedReducer
import com.branhamplayer.android.sermons.actions.ProfileAction
import com.branhamplayer.android.sermons.states.SermonsState
import javax.inject.Inject

class ProfileReducer @Inject constructor() : TypedReducer<ProfileAction, SermonsState> {

    override fun invoke(action: ProfileAction, oldState: SermonsState) = when (action) {
        is ProfileAction.GetUserProfileAction -> oldState
        is ProfileAction.SaveUserProfileAction -> saveUserProfile(action.userProfile, oldState)
    }

    private fun saveUserProfile(userProfile: UserProfile, oldState: SermonsState) = oldState.copy(
        profile = userProfile
    )
}
