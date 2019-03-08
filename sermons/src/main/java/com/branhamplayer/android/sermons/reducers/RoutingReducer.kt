package com.branhamplayer.android.sermons.reducers

import com.branhamplayer.android.base.redux.TypedReducer
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

    override fun invoke(action: RoutingAction, oldState: SermonsState): SermonsState {
        when (action) {
            is RoutingAction.NavigateToNoSelectionAction -> navigateToNoSelection()
            is RoutingAction.NavigateToPlayerAction -> navigateToPlayer()
        }

        return oldState
    }

    // endregion

    private fun navigateToNoSelection() {
        sermonsActivity.setDetailFragment(noSelectionFragment)
    }

    private fun navigateToPlayer() {
        sermonsActivity.setDetailFragment(playerFragment)
    }
}
