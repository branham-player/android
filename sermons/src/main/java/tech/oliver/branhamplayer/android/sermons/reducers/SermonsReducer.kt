package tech.oliver.branhamplayer.android.sermons.reducers

import org.rekotlin.Action
import tech.oliver.branhamplayer.android.sermons.actions.DataAction
import tech.oliver.branhamplayer.android.sermons.states.SermonsState

class SermonsReducer {
    companion object {

        fun reduce(action: Action, sermonsState: SermonsState?): SermonsState {
            var state = sermonsState ?: SermonsState()

            state = when(action) {
                is DataAction -> DataReducer.reduce(action, state)
                else -> state
            }

            return state
        }
    }
}