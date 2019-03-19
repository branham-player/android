package com.branhamplayer.android.sermons.di

import com.branhamplayer.android.sermons.ui.SermonsActivity

object DaggerInjector {

    // region Player

    // This component needs to be rebuilt every time. Since rotating the device
    // destroys the given activity, a new one must be available on each rotate
    fun buildPlayerComponent(activity: SermonsActivity): PlayerComponent = DaggerPlayerComponent
        .builder()
        .playerModule(PlayerModule(activity))
        .build()

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