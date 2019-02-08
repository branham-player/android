package com.branhamplayer.android.sermons.states

import com.auth0.android.result.UserProfile
import com.branhamplayer.android.sermons.models.SermonModel
import org.rekotlin.StateType

data class SermonsState(
    val drawerItemSelectedIndex: Int = 0,
    val profile: UserProfile? = null,
    val sermonList: List<SermonModel>? = null,
    val title: String? = null
) : StateType
