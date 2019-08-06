package com.branhamplayer.android.sermons.states

import com.branhamplayer.android.auth.utils.User
import com.branhamplayer.android.sermons.models.SermonModel
import com.branhamplayer.android.sermons.utils.permissions.PermissionManager
import org.rekotlin.StateType

data class SermonsState(
    val drawerItemSelectedIndex: Int = 0,
    val fileReadPermission: PermissionManager.PermissionStatus = PermissionManager.PermissionStatus.DeniedOnce,
    val profile: User.Profile? = null,
    val sermonList: List<SermonModel>? = null
) : StateType
