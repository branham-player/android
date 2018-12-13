package tech.oliver.branhamplayer.android.sermons.actions

import android.content.Context
import org.rekotlin.Action

sealed class AlertAction: Action {
    data class ShowPermissionDeniedErrorAction(val context: Context) : AlertAction()
}
