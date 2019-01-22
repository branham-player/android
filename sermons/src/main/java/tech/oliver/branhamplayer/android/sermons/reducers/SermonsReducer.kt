package tech.oliver.branhamplayer.android.sermons.reducers

import org.rekotlin.Action
import tech.oliver.branhamplayer.android.sermons.actions.DataAction
import tech.oliver.branhamplayer.android.sermons.actions.DrawerAction
import tech.oliver.branhamplayer.android.sermons.actions.ProfileAction
import tech.oliver.branhamplayer.android.sermons.states.SermonsState

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