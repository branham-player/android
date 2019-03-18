package com.branhamplayer.android.sermons.states

import com.auth0.android.result.UserProfile
import com.branhamplayer.android.sermons.models.SermonModel
import org.rekotlin.StateType

data class SermonsState(
    // region Auth
    val profile: UserProfile? = null,
    // endregion

    // region Drawer
    val drawerItemSelectedIndex: Int = 0,
    // endregion

    // region Player
    val selectedSermon: SermonModel? = null,
    // endregion

    // region SermonList
    val sermons: List<SermonModel>? = null,
    val title: String? = null
    // endregion
) : StateType
