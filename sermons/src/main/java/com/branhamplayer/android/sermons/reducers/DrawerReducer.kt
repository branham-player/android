package com.branhamplayer.android.sermons.reducers

import com.branhamplayer.android.sermons.actions.DrawerAction
import com.branhamplayer.android.sermons.states.SermonsState

class DrawerReducer {
    companion object {

        fun reduce(action: DrawerAction, oldState: SermonsState) = when (action) {
            is DrawerAction.SetSelectedItemAction -> setSelectedItem(oldState, action)
        }

        private fun setSelectedItem(oldState: SermonsState, action: DrawerAction.SetSelectedItemAction) = oldState.copy(
            drawerItemSelectedIndex = action.index
        )
    }
}
