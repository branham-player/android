package com.branhamplayer.android.sermons.reducers

import com.branhamplayer.android.sermons.actions.AuthAction
import com.branhamplayer.android.sermons.actions.SermonListAction
import com.branhamplayer.android.sermons.actions.DrawerAction
import com.branhamplayer.android.sermons.actions.PlayerAction
import com.branhamplayer.android.sermons.di.DaggerInjector
import com.branhamplayer.android.sermons.states.SermonsState
import org.rekotlin.Action
import org.rekotlin.Reducer
import javax.inject.Inject

class SermonsReducer : Reducer<SermonsState> {

    // region DI

    @Inject
    lateinit var authReducer: AuthReducer

    @Inject
    lateinit var drawerReducer: DrawerReducer

    @Inject
    lateinit var playerReducer: PlayerReducer

    @Inject
    lateinit var sermonListReducer: SermonListReducer

    // endregion

    // region Reducer

    override fun invoke(action: Action, sermonsState: SermonsState?): SermonsState {
        val oldState = sermonsState ?: SermonsState()

        return when (action) {
            is AuthAction -> {
                inject()
                authReducer.invoke(action, oldState)
            }

            is DrawerAction -> {
                inject()
                drawerReducer.invoke(action, oldState)
            }

            is PlayerAction -> {
                inject()
                playerReducer.invoke(action, oldState)
            }

            is SermonListAction -> {
                inject()
                sermonListReducer.invoke(action, oldState)
            }

            else -> oldState
        }
    }

    // endregion

    private fun inject() {
        DaggerInjector.sermonsComponent?.inject(this)
    }
}
