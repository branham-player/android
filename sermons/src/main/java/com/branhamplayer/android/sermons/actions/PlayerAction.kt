package com.branhamplayer.android.sermons.actions

import com.branhamplayer.android.sermons.models.SermonModel

sealed class PlayerAction : SermonsAction {
    object NavigateToNoSelectionAction : PlayerAction()
    data class NavigateToPlayerAction(val selectedSermon: SermonModel) : PlayerAction()
}
