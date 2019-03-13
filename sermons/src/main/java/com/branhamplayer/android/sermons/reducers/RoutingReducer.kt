package com.branhamplayer.android.sermons.reducers

import com.branhamplayer.android.base.redux.TypedReducer
import com.branhamplayer.android.R as RBase
import com.branhamplayer.android.sermons.actions.RoutingAction
import com.branhamplayer.android.sermons.states.SermonsState
import com.branhamplayer.android.sermons.ui.NoSelectionFragment
import com.branhamplayer.android.sermons.ui.PlayerFragment
import com.branhamplayer.android.sermons.ui.SermonsActivity
import javax.inject.Inject

class RoutingReducer @Inject constructor(
    private val sermonsActivity: SermonsActivity,
    private val noSelectionFragment: NoSelectionFragment,
    private val playerFragment: PlayerFragment
) : TypedReducer<RoutingAction, SermonsState> {

    // region TypedReducer

    override fun invoke(action: RoutingAction, oldState: SermonsState) = when (action) {
        is RoutingAction.NavigateToNoSelectionAction -> navigateToNoSelection(oldState)
        is RoutingAction.NavigateToPlayerAction -> navigateToPlayer(action, oldState)
    }

    // endregion

    private fun navigateToNoSelection(oldState: SermonsState): SermonsState {
        sermonsActivity.setDetailFragment(noSelectionFragment)
        return oldState
    }

    private fun navigateToPlayer(
        action: RoutingAction.NavigateToPlayerAction,
        oldState: SermonsState
    ): SermonsState {
        val isTablet = sermonsActivity.resources.getBoolean(RBase.bool.is_tablet)

        if (isTablet) {
            sermonsActivity.setDetailFragment(playerFragment)
        } else {
            sermonsActivity.setMasterFragment(playerFragment)
        }

        return oldState.copy(
            selectedSermon = action.sermon
        )
    }
}
