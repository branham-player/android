package com.branhamplayer.android.sermons.actions

import com.branhamplayer.android.base.redux.BaseAction

sealed class DrawerAction : BaseAction {
    data class SetSelectedItemAction(val index: Int) : DrawerAction()
}
