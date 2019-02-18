package com.branhamplayer.android.sermons.reducers

import com.branhamplayer.android.sermons.actions.DataAction
import com.branhamplayer.android.sermons.actions.DrawerAction
import com.branhamplayer.android.sermons.actions.ProfileAction
import com.branhamplayer.android.sermons.states.SermonsState
import org.rekotlin.Action
import org.rekotlin.Reducer

class SermonsReducer : Reducer<SermonsState> {

    // TODO: Move these to Dagger, when implemented
    private val dataReducer = DataReducer()
    private val drawerReducer = DrawerReducer()
    private val profileReducer = ProfileReducer()

    override fun invoke(action: Action, sermonsState: SermonsState?): SermonsState {
        val oldState = sermonsState ?: SermonsState()

        return when (action) {
            is DataAction -> dataReducer.invoke(action, oldState)
            is DrawerAction -> drawerReducer.invoke(action, oldState)
            is ProfileAction -> profileReducer.invoke(action, oldState)
            else -> oldState
        }
    }
}
