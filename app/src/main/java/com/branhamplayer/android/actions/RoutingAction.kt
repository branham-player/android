package com.branhamplayer.android.actions

import com.branhamplayer.android.base.redux.BaseAction

sealed class RoutingAction : BaseAction {
    object CloseAppAction : RoutingAction()

    object NavigateToAuthenticationAction : RoutingAction()
    object NavigateToGooglePlayStoreAction : RoutingAction()
    object NavigateToSermonsAction : RoutingAction()
    object NavigateToWelcomeAction : RoutingAction()
}
