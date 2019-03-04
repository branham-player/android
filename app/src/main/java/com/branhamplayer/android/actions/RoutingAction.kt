package com.branhamplayer.android.actions

import org.rekotlin.Action

sealed class RoutingAction : Action {
    object NavigateToSermonsAction : RoutingAction()
    object ShowLoginErrorAction : RoutingAction()
}
