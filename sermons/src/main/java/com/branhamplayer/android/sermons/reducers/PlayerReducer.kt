package com.branhamplayer.android.sermons.reducers

import com.branhamplayer.android.base.redux.TypedReducer
import com.branhamplayer.android.sermons.actions.PlayerAction
import com.branhamplayer.android.R as RBase
import com.branhamplayer.android.sermons.states.SermonsState
import com.branhamplayer.android.sermons.ui.NoSelectionFragment
import com.branhamplayer.android.sermons.ui.PlayerFragment
import com.branhamplayer.android.sermons.ui.SermonsActivity
import javax.inject.Inject

class PlayerReducer @Inject constructor(
    private val sermonsActivity: SermonsActivity,
    private val noSelectionFragment: NoSelectionFragment,
    private val playerFragment: PlayerFragment
) : TypedReducer<PlayerAction, SermonsState> {

    // region TypedReducer

    override fun invoke(action: PlayerAction, oldState: SermonsState) = when (action) {
        is PlayerAction.HidePhoneActionBarAction -> hidePhoneActionBar(oldState)
        is PlayerAction.NavigateToNoSelectionAction -> navigateToNoSelection(oldState)
        is PlayerAction.NavigateToPlayerAction -> navigateToPlayer(oldState, action)
        is PlayerAction.ShowBackButtonAction -> showBackButton(oldState, action)
    }

    // endregion

    private fun hidePhoneActionBar(oldState: SermonsState) = oldState.copy(
        phoneActionBarVisible = false
    )

    private fun navigateToNoSelection(oldState: SermonsState): SermonsState {
        sermonsActivity.setDetailFragment(noSelectionFragment)
        return oldState
    }

    private fun navigateToPlayer(oldState: SermonsState, action: PlayerAction.NavigateToPlayerAction): SermonsState {
        val isLargeTablet = sermonsActivity.resources.getBoolean(RBase.bool.is_large_tablet)

        if (isLargeTablet) {
            sermonsActivity.setDetailFragment(playerFragment)
        } else {
            sermonsActivity.setMasterFragment(playerFragment)
        }

        return oldState.copy(
            selectedSermon = action.selectedSermon
        )
    }

    private fun showBackButton(oldState: SermonsState, action: PlayerAction.ShowBackButtonAction) = oldState.copy(
        showPlayerBackButton = action.showButton
    )
}
