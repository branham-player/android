package tech.oliver.branhamplayer.android.startup.actions

import android.content.Context
import org.rekotlin.Action

sealed class RoutingAction : Action {
    data class NavigateToSermonsAction(val context: Context) : RoutingAction()
    class ShowLoginErrorAction : RoutingAction()
}
