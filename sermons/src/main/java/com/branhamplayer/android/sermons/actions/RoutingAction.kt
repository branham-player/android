package com.branhamplayer.android.sermons.actions

import com.branhamplayer.android.sermons.models.SermonModel
import org.rekotlin.Action

sealed class RoutingAction : Action {
    object NavigateToNoSelectionAction : RoutingAction()
    data class NavigateToPlayerAction(val sermon: SermonModel) : RoutingAction()
}
