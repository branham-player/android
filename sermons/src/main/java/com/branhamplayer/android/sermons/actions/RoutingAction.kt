package com.branhamplayer.android.sermons.actions

import org.rekotlin.Action

sealed class RoutingAction : Action {
    object NavigateToNoSelectionAction : RoutingAction()
}
