package com.branhamplayer.android.sermons.actions

import com.branhamplayer.android.base.redux.BaseAction

sealed class RoutingAction : BaseAction {
    object NavigateToApplicationSettings : RoutingAction()
}
