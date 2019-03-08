package com.branhamplayer.android.sermons.reducers

import com.branhamplayer.android.base.redux.TypedReducer
import com.branhamplayer.android.sermons.actions.RoutingAction
import com.branhamplayer.android.sermons.states.SermonsState
import com.branhamplayer.android.sermons.ui.NoSelectionFragment
import com.branhamplayer.android.sermons.ui.SermonsActivity
import javax.inject.Inject

class RoutingReducer @Inject constructor(
    private val sermonsActivity: SermonsActivity,
    private val noSelectionFragment: NoSelectionFragment
) : TypedReducer<RoutingAction, SermonsState> {

    override fun invoke(action: RoutingAction, oldState: SermonsState): SermonsState {
        when (action) {
            is RoutingAction.NavigateToNoSelectionAction -> navigateToNoSelection()
        }

        return oldState
    }

    private fun navigateToNoSelection() {
        sermonsActivity.setDetailFragment(noSelectionFragment)
    }
}
