package com.branhamplayer.android.sermons.reducers

import com.branhamplayer.android.sermons.actions.DataAction
import com.branhamplayer.android.sermons.actions.DrawerAction
import com.branhamplayer.android.sermons.actions.ProfileAction
import com.branhamplayer.android.sermons.actions.RoutingAction
import com.branhamplayer.android.sermons.di.DaggerInjector
import com.branhamplayer.android.sermons.states.SermonsState
import org.rekotlin.Action
import org.rekotlin.Reducer
import javax.inject.Inject

class SermonsReducer : Reducer<SermonsState> {

    // region DI

    @Inject
    lateinit var dataReducer: DataReducer

    @Inject
    lateinit var drawerReducer: DrawerReducer

    @Inject
    lateinit var profileReducer: ProfileReducer

    @Inject
    lateinit var routingReducer: RoutingReducer

    // endregion

    // region Reducer

    override fun invoke(action: Action, sermonsState: SermonsState?): SermonsState {
        val oldState = sermonsState ?: SermonsState()

        return when (action) {
            is DataAction -> {
                inject()
                dataReducer.invoke(action, oldState)
            }

            is DrawerAction -> {
                inject()
                drawerReducer.invoke(action, oldState)
            }

            is ProfileAction -> {
                inject()
                profileReducer.invoke(action, oldState)
            }

            is RoutingAction -> {
                inject()
                routingReducer.invoke(action, oldState)
            }

            else -> oldState
        }
    }

    // endregion

    private fun inject() {
        DaggerInjector.sermonsComponent?.inject(this)
    }
}
