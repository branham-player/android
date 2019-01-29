package com.branhamplayer.android.sermons.reducers

import org.rekotlin.Action
import com.branhamplayer.android.sermons.actions.DataAction
import com.branhamplayer.android.sermons.actions.DrawerAction
import com.branhamplayer.android.sermons.actions.ProfileAction
import com.branhamplayer.android.sermons.states.SermonsState

class SermonsReducer {
    companion object {

        fun reduce(action: Action, sermonsState: SermonsState?): SermonsState {
            val state = sermonsState ?: SermonsState()

            return when (action) {
                is DataAction -> DataReducer.reduce(action, state)
                is DrawerAction -> DrawerReducer.reduce(action, state)
                is ProfileAction -> ProfileReducer.reduce(action, state)
                else -> state
            }
        }
    }
}