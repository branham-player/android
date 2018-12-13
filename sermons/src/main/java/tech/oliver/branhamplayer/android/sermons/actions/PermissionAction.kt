package tech.oliver.branhamplayer.android.sermons.actions

import android.app.Activity
import org.rekotlin.Action

sealed class PermissionAction : Action {
    data class GetFileReadPermissionAction(val activity: Activity) : PermissionAction()
}