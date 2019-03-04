package com.branhamplayer.android.sermons.reducers

import com.branhamplayer.android.base.redux.TypedReducer
import com.branhamplayer.android.sermons.actions.DrawerAction
import com.branhamplayer.android.sermons.states.SermonsState
import javax.inject.Inject

class DrawerReducer @Inject constructor() : TypedReducer<DrawerAction, SermonsState> {

    override fun invoke(action: DrawerAction, oldState: SermonsState) = when (action) {
        is DrawerAction.SetSelectedItemAction -> setSelectedItem(oldState, action)
    }

    private fun setSelectedItem(oldState: SermonsState, action: DrawerAction.SetSelectedItemAction) = oldState.copy(
        drawerItemSelectedIndex = action.index
    )
}
