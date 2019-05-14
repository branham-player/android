package com.branhamplayer.android.sermons.reducers

import com.branhamplayer.android.base.redux.BaseAction
import com.branhamplayer.android.sermons.actions.AuthenticationAction
import com.branhamplayer.android.sermons.actions.DrawerAction
import com.branhamplayer.android.sermons.actions.SermonListAction
import com.branhamplayer.android.sermons.dagger.DaggerInjector
import com.branhamplayer.android.sermons.states.SermonsState
import org.rekotlin.Action
import org.rekotlin.Reducer
import javax.inject.Inject

class SermonsReducer : Reducer<SermonsState> {

    // region Dagger

    @Inject
    lateinit var authenticationReducer: AuthenticationReducer

    @Inject
    lateinit var drawerReducer: DrawerReducer

    @Inject
    lateinit var sermonListReducer: SermonListReducer

    // endregion

    override fun invoke(action: Action, sermonsState: SermonsState?): SermonsState {
        val oldState = sermonsState ?: SermonsState()

        if (action is BaseAction) {
            DaggerInjector.sermonsComponent?.inject(this)
        }

        return when (action) {
            is AuthenticationAction -> authenticationReducer.invoke(action, oldState)
            is DrawerAction -> drawerReducer.invoke(action, oldState)
            is SermonListAction -> sermonListReducer.invoke(action, oldState)
            else -> oldState
        }
    }
}
