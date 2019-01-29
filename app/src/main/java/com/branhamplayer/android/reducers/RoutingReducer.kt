package com.branhamplayer.android.reducers

import android.content.Context
import android.content.Intent
import com.branhamplayer.android.actions.RoutingAction
import com.branhamplayer.android.states.StartupState
import org.koin.standalone.StandAloneContext

class RoutingReducer {
    companion object {

        fun reduce(action: RoutingAction, startupState: StartupState): StartupState {
            when (action) {
                is RoutingAction.NavigateToSermonsAction -> navigateToSermons(action.context)
            }

            return startupState
        }

        private fun navigateToSermons(context: Context) {
            val sermonIntent: Intent = StandAloneContext.getKoin().koinContext.get()
            sermonIntent.addCategory(Intent.CATEGORY_BROWSABLE)
            sermonIntent.flags = Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK
            sermonIntent.setPackage(context.packageName)

            context.startActivity(sermonIntent)
        }
    }
}
