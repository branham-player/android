package com.branhamplayer.android.reducers

import android.content.Context
import android.content.Intent
import com.branhamplayer.android.actions.RoutingAction
import com.branhamplayer.android.base.redux.TypedReducer
import com.branhamplayer.android.di.DaggerInjector
import com.branhamplayer.android.states.StartupState
import javax.inject.Inject

class RoutingReducer @Inject constructor(
    private val context: Context,
    private val sermonsIntent: Intent
) : TypedReducer<RoutingAction, StartupState> {

    override fun invoke(action: RoutingAction, oldState: StartupState): StartupState {
        when (action) {
            is RoutingAction.NavigateToSermonsAction -> navigateToSermons()
            is RoutingAction.ShowLoginErrorAction -> Unit
        }

        return oldState
    }

    private fun navigateToSermons() {
        sermonsIntent.addCategory(Intent.CATEGORY_BROWSABLE)
        sermonsIntent.flags = Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK
        sermonsIntent.setPackage(context.packageName)

        context.startActivity(sermonsIntent)
    }
}
