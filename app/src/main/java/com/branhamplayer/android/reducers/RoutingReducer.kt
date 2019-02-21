package com.branhamplayer.android.reducers

import android.content.Intent
import com.branhamplayer.android.actions.RoutingAction
import com.branhamplayer.android.base.redux.TypedReducer
import com.branhamplayer.android.states.StartupState
import org.koin.standalone.StandAloneContext

class RoutingReducer : TypedReducer<RoutingAction, StartupState> {

    override fun invoke(action: RoutingAction, oldState: StartupState): StartupState {
        when (action) {
            is RoutingAction.NavigateToSermonsAction -> navigateToSermons(action)
            is RoutingAction.ShowLoginErrorAction -> Unit
        }

        return oldState
    }

    private fun navigateToSermons(action: RoutingAction.NavigateToSermonsAction) {
        val sermonIntent: Intent = StandAloneContext.getKoin().koinContext.get()
        sermonIntent.addCategory(Intent.CATEGORY_BROWSABLE)
        sermonIntent.flags = Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK
        sermonIntent.setPackage(action.context.packageName)

        action.context.startActivity(sermonIntent)
    }
}
