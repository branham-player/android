package com.branhamplayer.android.actions

import org.rekotlin.Action

sealed class RoutingAction : Action {
    object CloseAppAction : RoutingAction()

    object NavigateToAuthenticationAction : RoutingAction()
    object NavigateToGooglePlayStoreAction : RoutingAction()
    object NavigateToSermonsAction : RoutingAction()
    object NavigateToWelcomeAction : RoutingAction()
}
