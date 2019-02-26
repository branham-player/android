package com.branhamplayer.android.reducers

import android.content.Context
import android.content.Intent
import com.branhamplayer.android.actions.RoutingAction
import com.branhamplayer.android.base.redux.TypedReducer
import com.branhamplayer.android.di.DaggerInjector
import com.branhamplayer.android.states.StartupState
import javax.inject.Inject

class RoutingReducer : TypedReducer<RoutingAction, StartupState> {

    @Inject
    @JvmField
    var context: Context? = null

    @Inject
    @JvmField
    var sermonsIntent: Intent? = null

    override fun invoke(action: RoutingAction, oldState: StartupState): StartupState {
        when (action) {
            is RoutingAction.NavigateToSermonsAction -> navigateToSermons()
            is RoutingAction.ShowLoginErrorAction -> Unit
        }

        return oldState
    }

    private fun navigateToSermons() {
        inject()

        sermonsIntent?.addCategory(Intent.CATEGORY_BROWSABLE)
        sermonsIntent?.flags = Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK

        context?.let {
            sermonsIntent?.setPackage(it.packageName)
            it.startActivity(sermonsIntent)
        }
    }

    private fun inject() {
        DaggerInjector.routingComponent?.inject(this)
    }
}
