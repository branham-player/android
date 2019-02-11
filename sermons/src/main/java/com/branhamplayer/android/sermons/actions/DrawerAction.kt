package com.branhamplayer.android.sermons.actions

import org.rekotlin.Action

sealed class DrawerAction : Action {
    data class SetSelectedItemAction(val index: Int) : DrawerAction()
}
