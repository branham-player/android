package com.branhamplayer.android.sermons.di

import android.content.Context
import com.branhamplayer.android.sermons.ui.SermonsActivity

object DaggerInjector {

    // region Player

    var playerComponent: PlayerComponent? = null
        private set

    fun buildPlayerComponent(context: Context): PlayerComponent {
        val component = playerComponent ?: DaggerPlayerComponent
            .builder()
            .playerModule(PlayerModule(context))
            .build()

        playerComponent = component
        return component
    }

    // endregion

    // region Activity & Module-wide Injections

    var sermonsComponent: SermonsComponent? = null
        private set

    fun buildSermonsComponent(activity: SermonsActivity): SermonsComponent {
        val component = sermonsComponent ?: DaggerSermonsComponent
            .builder()
            .playerModule(PlayerModule(activity))
            .sermonsModule(SermonsModule(activity))
            .build()

        sermonsComponent = component
        return component
    }

    // endregion
}