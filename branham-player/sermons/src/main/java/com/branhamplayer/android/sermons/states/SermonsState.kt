package com.branhamplayer.android.sermons.states

import com.auth0.android.result.UserProfile
import com.branhamplayer.android.sermons.models.SermonModel
import com.mikepenz.materialdrawer.AccountHeader
import com.mikepenz.materialdrawer.Drawer
import org.rekotlin.StateType

data class SermonsState(
        val drawer: Drawer? = null,
        val drawerAccountHeader: AccountHeader? = null,
        val drawerItemSelectedIndex: Int = 0,
        val profile: UserProfile? = null,
        val sermonList: List<SermonModel>? = null,
        val title: String? = null
) : StateType
